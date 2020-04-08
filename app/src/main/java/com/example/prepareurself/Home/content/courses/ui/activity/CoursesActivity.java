package com.example.prepareurself.Home.content.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.prepareurself.Home.content.courses.ui.fragments.ProjectsFragment;
import com.example.prepareurself.Home.content.courses.ui.fragments.ResourcesFragment;
import com.example.prepareurself.Home.content.resources.ui.adapter.SectionsPagerAdapter;
import com.example.prepareurself.R;
import com.google.android.material.tabs.TabLayout;

public class CoursesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        viewPager = findViewById(R.id.view_pager_courses);
        tabs = findViewById(R.id.tabs_courses);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Tech Stack");

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
