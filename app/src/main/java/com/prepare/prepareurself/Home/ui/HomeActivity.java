package com.prepare.prepareurself.Home.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.ui.fragment.DashboardFragment;
import com.prepare.prepareurself.Home.viewmodel.HomeActivityViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.utils.Constants;
import com.google.android.material.navigation.NavigationView;
import com.prepare.prepareurself.utils.Utility;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DashboardFragment.HomeActivityInteractor,
        View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvNameNavHeader;
    private HomeActivityViewModel viewModel;
    private NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

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

        }



        return true;

    }

    private void shareApp() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_logo_share);
        try {
            Uri uri = Utility.getUriOfBitmap(icon, this);
            String text = "Prepareurself is preparing me for my internships. I found some best and amazing Project works, a wide range of latest Tech-Stacks and some best Resources from internet at one place.\n" +
                    "Checkout our prepareurself app.\n\n"+
                    "Click link to install\n"+
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
    public void onTopicClicked(SuggestedTopicsModel topicsModel) {
        Intent intent = new Intent(HomeActivity.this, ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onProjectClicked(SuggestedProjectModel projectsModel) {
        Intent intent = new Intent(HomeActivity.this, ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
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
