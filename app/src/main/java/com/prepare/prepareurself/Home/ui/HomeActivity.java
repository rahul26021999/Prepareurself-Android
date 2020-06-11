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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.aboutus.ui.AboutusFragment;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.AllCoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.CourseDetailActivity;
import com.prepare.prepareurself.courses.ui.activity.CourseDetalActivity;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.banner.BannerModel;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabProjectctivity;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabResourceActivity;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.ui.fragment.DashboardFragment;
import com.prepare.prepareurself.Home.viewmodel.HomeActivityViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.favourites.ui.FavoritesActivity;
import com.prepare.prepareurself.favourites.ui.FavouritesFragment;
import com.prepare.prepareurself.feedback.ui.FeedbackFragment;
import com.prepare.prepareurself.firebase.UpdateHelper;
import com.prepare.prepareurself.preferences.ui.PreferencesActivity;
import com.prepare.prepareurself.profile.ui.fragments.ProfileFragment;
import com.prepare.prepareurself.quizv2.ui.QuizActivity;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.prepare.prepareurself.utils.HomeViewPagerAdapter;
import com.prepare.prepareurself.utils.NonSwipableViewPager;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
        UpdateHelper.OnUpdateCheckListener,
        AboutusFragment.AboutUsHomeInteractor,
        FeedbackFragment.FeedBackHomeInteractor,
        ProfileFragment.ProfileHomeInteractor,
        FavouritesFragment.FavouritesHomeInteractor {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvNameNavHeader;
    private HomeActivityViewModel viewModel;
    private NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView profileImageView;
    private PrefManager prefManager;
    public static boolean gotoPrefFromBanner = false;
    private NonSwipableViewPager viewPager;
    SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);

        searchFragment = SearchFragment.newInstance();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.main_view_pager);

        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        View navHeaderView = navigationView.getHeaderView(0);
        tvNameNavHeader = navHeaderView.findViewById(R.id.tv_user_name_nav_header);
        profileImageView = navHeaderView.findViewById(R.id.profile_image);

        HomeViewPagerAdapter viewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_dashboard, R.id.nav_profile, R.id.nav_contact_us)
//                .setDrawerLayout(drawer)
//                .build();
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);



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

                            if (userModel.getProfile_image().contains("https") || userModel.getProfile_image().contains("http")){
                                Glide.with(HomeActivity.this)
                                        .load(userModel.getProfile_image())
                                        .override(300,300)
                                        .override(300,300)
                                        .placeholder(R.drawable.person_placeholder)
                                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                        .into(profileImageView);
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


                    Log.d("tojen_debug", "before onResponse: "+userModel.getAndroid_token());
                    if (TextUtils.isEmpty(userModel.getAndroid_token()) || userModel.getAndroid_token() == null){
                        viewModel.updateToken(prefManager.getString(Constants.JWTTOKEN),Utility.getOneSignalId());
                    }

             //       viewModel.updateToken(prefManager.getString(Constants.JWTTOKEN),Utility.getOneSignalId());

                }else{
                    viewModel.getUser(prefManager.getString(Constants.JWTTOKEN), HomeActivity.this);
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

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    public void navigateHome(int position){
        viewPager.setCurrentItem(position);
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
                //intent to course actvty to check
                /*Intent intentcourse = new Intent(HomeActivity.this, CourseDetailActivity.class);
                startActivity(intentcourse);*/
                break;
            case R.id.nav_profile :
                viewPager.setCurrentItem(1);
                break;
            case R.id.nav_dashboard :
                viewPager.setCurrentItem(0);
                break;
            case R.id.nav_star:
                //redirectToPlayStore();
                Intent intent = new Intent(HomeActivity.this, CourseDetalActivity.class);
                intent.putExtra(Constants.COURSEID,1);
                intent.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent);
                break;
            case R.id.nav_share:
                shareApp();
                /*Intent intent1 = new Intent(HomeActivity.this, TabResourceActivity.class);
                intent1.putExtra(Constants.COURSEID,1);
                intent1.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent1);*/
                break;
            case R.id.nav_about_us :
                startActivity(new Intent(HomeActivity.this, QuizActivity.class));
                //viewPager.setCurrentItem(4);
                break;
            case R.id.nav_feedback :
                startActivity(new Intent(HomeActivity.this, PreferencesActivity.class));
                //viewPager.setCurrentItem(3);
                break;
            case R.id.nav_fav:
                viewPager.setCurrentItem(2);
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

                case "project":
                    Intent intentProject = new Intent(this, ProjectsActivity.class);
                    intentProject.putExtra(Constants.PROJECTID,bannerModel.getScreen_id());
                    startActivity(intentProject);

            }
        }
    }

    private void shareApp() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.namelogo);
        try {
            Uri uri = Utility.getUriOfBitmapJPG(icon, this);
            String text = "Prepareurself is providing various courses, projects and resources. " +
                    "One place to learn skills and test them by developing projects. \n" +
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
        Intent intent = new Intent(HomeActivity.this,CourseDetailActivity.class);
        intent.putExtra(Constants.COURSEID,courseModel.getId());
        //intent.putExtra(Constants.COURSENAME,courseModel.getName());
        startActivity(intent);
        Log.d("COURSEID",""+courseModel.getId());
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
    public void onResourceCliked(final ResourceModel resourceModel) {
        if (resourceModel.getType().equalsIgnoreCase("video")){

            if (resourceModel.getLink().contains("youtu.be") || resourceModel.getLink().contains("youtube")){
                Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
                intent.putExtra(Constants.VIDEOCODE, Utility.getVideoCode(resourceModel.getLink()));
                intent.putExtra(Constants.VIDEOTITLE, resourceModel.getTitle());
                intent.putExtra(Constants.RESOURCEID, resourceModel.getId());
                intent.putExtra(Constants.VIDEODESCRIPTION, resourceModel.getDescription());
                intent.putExtra(Constants.RESOURCEVIDEO, true);
                startActivity(intent);
            }else {
                Utility.redirectUsingCustomTab(HomeActivity.this, resourceModel.getLink());
                Log.d("resource_viewed","beforeliked : "+resourceModel.getView()+", "+resourceModel.getTotal_views()+", "+resourceModel.getId());
                if (resourceModel.getView() == 0){
                    viewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN), resourceModel.getId())
                            .observeForever(new Observer<ResourceViewsResponse>() {
                                @Override
                                public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                    if (resourceViewsResponse.getError_code() == 0){
                                        resourceModel.setView(1);
                                        Log.d("resource_viewed","onliked begore: "+resourceModel.getView()+", "+resourceModel.getTotal_views()+", "+resourceModel.getId());
                                        resourceModel.setTotal_views(resourceModel.getTotal_views()+1);
                                        Log.d("resource_viewed","onliked : "+resourceModel.getView()+", "+resourceModel.getTotal_views()+", "+resourceModel.getId());
                                        viewModel.saveResource(resourceModel);
                                    }
                                }
                            });
                }
            }

        }else if (resourceModel.getType().equalsIgnoreCase("theory")){
            Utility.redirectUsingCustomTab(HomeActivity.this, resourceModel.getLink());
            Log.d("resource_viewed","beforeliked : "+resourceModel.getView()+", "+resourceModel.getTotal_views()+", "+resourceModel.getId());
            if (resourceModel.getView() == 0){
                viewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN), resourceModel.getId())
                        .observeForever(new Observer<ResourceViewsResponse>() {
                            @Override
                            public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                if (resourceViewsResponse.getError_code() == 0){
                                    resourceModel.setView(1);
                                    Log.d("resource_viewed","onliked begore: "+resourceModel.getView()+", "+resourceModel.getTotal_views()+", "+resourceModel.getId());
                                    resourceModel.setTotal_views(resourceModel.getTotal_views()+1);
                                    Log.d("resource_viewed","onliked : "+resourceModel.getView()+", "+resourceModel.getTotal_views()+", "+resourceModel.getId());
                                    viewModel.saveResource(resourceModel);
                                }
                            }
                        });
            }
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
    public void openSearchFragment() {

        //frameLayout.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_container,searchFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void closeSearchFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount()>0){
            fm.popBackStack();
        }
    }

    @Override
    public void performSearch(String query) {
        searchFragment.performSearch(query);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFeedbackBackPressed() {
        navigateHome(0);
    }

    @Override
    public void onProfileBackPressed() {
        navigateHome(0);
    }

    @Override
    public void onAboutUsBackPressed() {
        navigateHome(0);
    }

    @Override
    public void onFavBackPressed() {
        navigateHome(0);
    }

}
