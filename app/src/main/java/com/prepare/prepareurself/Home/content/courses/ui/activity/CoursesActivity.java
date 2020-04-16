package com.prepare.prepareurself.Home.content.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.prepare.prepareurself.Home.content.courses.ui.fragments.ProjectsFragment;
import com.prepare.prepareurself.Home.content.courses.ui.fragments.ResourcesFragment;
import com.prepare.prepareurself.Home.content.courses.viewmodels.TopicViewModel;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.google.android.material.tabs.TabLayout;

public class CoursesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;

    public static int courseId;

    private TopicViewModel topicViewModel;

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        topicViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        viewPager = findViewById(R.id.view_pager_courses);
        tabs = findViewById(R.id.tabs_courses);

        prefManager = new PrefManager(CoursesActivity.this);

        Intent intent = getIntent();

        if (intent!=null){
            courseId = intent.getIntExtra(Constants.COURSEID, -1);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Tech Stack");

//        if (courseId != -1){
//            topicViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),courseId);
//        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(ProjectsFragment.newInstance(),"Projects");
        sectionsPagerAdapter.addFragment(ResourcesFragment.newInstance(),"Resources");
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
