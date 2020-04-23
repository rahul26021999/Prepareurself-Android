package com.prepare.prepareurself.Home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.ui.fragment.DashboardFragment;
import com.prepare.prepareurself.Home.viewmodel.HomeActivityViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.utils.Constants;
import com.google.android.material.navigation.NavigationView;

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

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DashboardFragment.HomeActivityInteractor {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvNameNavHeader;
    private HomeActivityViewModel viewModel;
    private NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawer;

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


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

       // navigationView.setNavigationItemSelectedListener(this);

        viewModel.retrieveUserData();

        viewModel.getUserModelLiveData().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                String name = userModel.getFirst_name() + " " + userModel.getLast_name();
                tvNameNavHeader.setText(name);
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
//        drawer.closeDrawers();
//
//        int id = item.getItemId();
//
//        switch (id){
//            case R.id.nav_dashboard :
//                navController.navigate(R.id.nav_dashboard_fragment);
//                break;
//            case R.id.nav_profile :
//                navController.navigate(R.id.nav_profile_fragment);
//                break;
//
//        }

        return true;

    }

    @Override
    public void onCourseClicked(CourseModel courseModel) {
        Intent intent = new Intent(HomeActivity.this,CoursesActivity.class);
        intent.putExtra(Constants.COURSEID,courseModel.getId());
        startActivity(intent);
    }

    @Override
    public void onTopicClicked(TopicsModel topicsModel) {
        Intent intent = new Intent(HomeActivity.this, ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
        Intent intent = new Intent(HomeActivity.this, ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
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
