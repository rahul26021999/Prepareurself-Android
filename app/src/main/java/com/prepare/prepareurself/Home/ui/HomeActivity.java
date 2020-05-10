package com.prepare.prepareurself.Home.ui;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.AllCoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.banner.BannerModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.ui.fragment.DashboardFragment;
import com.prepare.prepareurself.Home.viewmodel.HomeActivityViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.favourites.ui.FavoritesActivity;
import com.prepare.prepareurself.firebase.UpdateHelper;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        DashboardFragment.HomeActivityInteractor,
        View.OnClickListener,
        UpdateHelper.OnUpdateCheckListener {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvNameNavHeader;
    private HomeActivityViewModel viewModel;
    private NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView profileImageView;
    private PrefManager prefManager;
    public static boolean gotoPrefFromBanner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();



        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);

        View navHeaderView = navigationView.getHeaderView(0);
        tvNameNavHeader = navHeaderView.findViewById(R.id.tv_user_name_nav_header);
        profileImageView = navHeaderView.findViewById(R.id.profile_image);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_profile, R.id.nav_contact_us)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        viewModel.retrieveUserData();

        viewModel.getUserModelLiveData().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {


                if (userModel!=null){
                    String name = userModel.getFirst_name() + " " + userModel.getLast_name();
                    tvNameNavHeader.setText(name);

                    if (userModel.getProfile_image()!=null){
                        if (userModel.getProfile_image().endsWith(".svg")){
                            Utility.loadSVGImage(HomeActivity.this,Constants.USERIMAGEBASEURL + userModel.getProfile_image(), profileImageView);
                        }else{
                            Glide.with(HomeActivity.this)
                                    .load(Constants.USERIMAGEBASEURL + userModel.getProfile_image())
                                    .override(300,300)
                                    .placeholder(R.drawable.person_placeholder)
                                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                    .into(profileImageView);
                        }
                    }

                }

            }
        });

        prefManager = new PrefManager(this);

        Intent intent = getIntent();
        if (intent.getData()!=null){
            String data = intent.getData().toString().split("type=")[1];
            if (!TextUtils.isEmpty(data) && data.equals("feedback")){
                if (prefManager.getBoolean(Constants.ISLOGGEDIN)){
                    navController.navigate(R.id.nav_feedback);
                }else{
                    Intent loginIntent = new Intent(HomeActivity.this, AuthenticationActivity.class);
                    loginIntent.putExtra(Constants.FEEDBACKSHARE, true);
                    startActivity(loginIntent);
                }
            }else if (!TextUtils.isEmpty(data) && data.equals("profile]")){
                if (prefManager.getBoolean(Constants.ISLOGGEDIN)){
                    navController.navigate(R.id.nav_profile);
                }else{
                    Intent loginIntent = new Intent(HomeActivity.this, AuthenticationActivity.class);
                    loginIntent.putExtra(Constants.PROFILESHARE, true);
                    startActivity(loginIntent);
                }
            }
        }else if (intent.getBooleanExtra(Constants.FEEDBACKSHAREINTENT, false)){
            navController.navigate(R.id.nav_feedback);
        }else if (intent.getBooleanExtra(Constants.PROFILESHAREINTENT, false)){
            navController.navigate(R.id.nav_profile);
        }

    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Update Available")
                .setMessage("Please update to new version to continue use")
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.redirectUsingCustomTab(HomeActivity.this, urlApp);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
//
        drawer.closeDrawers();

        int id = item.getItemId();

        switch (id){
            case R.id.nav_contact_us :
                sendEmailToDeveloper();
                break;
            case R.id.nav_profile :
                navController.navigate(R.id.nav_profile);
                break;
            case R.id.nav_dashboard :
                navController.navigate(R.id.nav_dashboard);
                break;
            case R.id.nav_star:
                redirectToPlayStore();
                break;
            case R.id.nav_share:
                shareApp();
                break;
            case R.id.nav_about_us :
                navController.navigate(R.id.nav_about_us);
                break;
            case R.id.nav_feedback :
                navController.navigate(R.id.nav_feedback);
                break;
            case R.id.nav_fav:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;

        }



        return true;

    }


    @Override
    public void onBannerClicked(BannerModel bannerModel) {
        if (bannerModel!=null && bannerModel.getScreen()!=null){
            switch (bannerModel.getScreen()){
                case "allProject":
                    if (bannerModel.getCourse()!=null){
                        Intent intent = new Intent(this, CoursesActivity.class);
                        intent.putExtra(Constants.COURSEID, bannerModel.getCourse().getId());
                        intent.putExtra(Constants.COURSENAME, bannerModel.getCourse().getName());
                        intent.putExtra(Constants.SHOWPAGE,Constants.SHOWPROJECTS);
                        startActivity(intent);
                    }
                    break;
                case "allTopic":
                    if(bannerModel.getCourse()!=null){
                        Intent intent = new Intent(this, CoursesActivity.class);
                        intent.putExtra(Constants.COURSEID, bannerModel.getCourse().getId());
                        intent.putExtra(Constants.COURSENAME, bannerModel.getCourse().getName());
                        intent.putExtra(Constants.SHOWPAGE,Constants.SHOWTOPICS);
                        startActivity(intent);
                    }
                    break;

                case "allCourse":
                    Intent intent = new Intent(this, AllCoursesActivity.class);
                    startActivity(intent);
                    break;

                case "feedback":
                    navController.navigate(R.id.nav_feedback);
                    break;

                case "prefrence":
                    navController.navigate(R.id.nav_profile);
                    gotoPrefFromBanner = true;
                    break;

            }
        }
    }

    private void shareApp() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.namelogo);
        try {
            Uri uri = Utility.getUriOfBitmapJPG(icon, this);
            String text = "Prepareurself is providing various courses, projects and resources." +
                    "One place to learn skills and test them by developing projects.\n" +
                    "Checkout prepareurself app : \n" +
                    "prepareurself.in/install";
            Utility.shareContent(this,uri, text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void redirectToPlayStore() {
        Utility.redirectUsingCustomTab(this,Constants.GOOGLEPLAYLINK);
    }

    private void sendEmailToDeveloper() {
        String mailto = "mailto:contact@prepareurself.in"+
                "?subject=" + Uri.encode("Important Message");
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        }catch (ActivityNotFoundException e){
            Utility.showToast(this,"No App found for sending e-mail");
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCourseClicked(CourseModel courseModel) {
        Intent intent = new Intent(HomeActivity.this,CoursesActivity.class);
        intent.putExtra(Constants.COURSEID,courseModel.getId());
        intent.putExtra(Constants.COURSENAME,courseModel.getName());
        startActivity(intent);
    }

    @Override
    public void onTopicClicked(TopicsModel topicsModel) {
        Intent intent = new Intent(HomeActivity.this, ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        intent.putExtra(Constants.COURSEID, topicsModel.getCourse_id());
        startActivity(intent);
    }

    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
        Intent intent = new Intent(HomeActivity.this, ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onResourceCliked(ResourceModel resourceModel) {
        if (resourceModel.getType().equalsIgnoreCase("video")){

            if (resourceModel.getLink().contains("youtu.be") || resourceModel.getLink().contains("youtube")){
                Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
                intent.putExtra(Constants.VIDEOCODE, Utility.getVideoCode(resourceModel.getLink()));
                intent.putExtra(Constants.VIDEOTITLE, resourceModel.getTitle());
                intent.putExtra(Constants.VIDEODESCRIPTION, resourceModel.getDescription());
                intent.putExtra(Constants.SINGLEVIDEO, true);
                startActivity(intent);
            }else {
                Utility.redirectUsingCustomTab(HomeActivity.this, resourceModel.getLink());
            }

        }else if (resourceModel.getType().equalsIgnoreCase("theory")){
            Utility.redirectUsingCustomTab(HomeActivity.this, resourceModel.getLink());
        }
    }

    @Override
    public void onBarClicked() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
