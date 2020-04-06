package com.example.prepareurself.Home.content.courses.ui.activity;

import android.os.Bundle;

import com.example.prepareurself.Home.content.courses.ui.fragments.TheoryResourceFragment;
import com.example.prepareurself.Home.content.courses.ui.fragments.VideoResourceFragment;
import com.example.prepareurself.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prepareurself.Home.content.courses.ui.adapter.SectionsPagerAdapter;

public class CoursesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        viewPager = findViewById(R.id.view_pager_courses);
        tabs = findViewById(R.id.tabs);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(VideoResourceFragment.newInstance(),"Video Resources");
        sectionsPagerAdapter.addFragment(TheoryResourceFragment.newInstance(),"Theoretical Resources");
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);



    }
}