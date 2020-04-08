package com.example.prepareurself.Home.content.resources.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.prepareurself.Home.content.resources.ui.fragments.TheoryResourceFragment;
import com.example.prepareurself.Home.content.resources.ui.fragments.VideoResourceFragment;
import com.example.prepareurself.Home.content.resources.ui.adapter.SectionsPagerAdapter;
import com.example.prepareurself.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        viewPager = findViewById(R.id.view_pager_resources);
        tabs = findViewById(R.id.tabs_topics);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Resources");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(VideoResourceFragment.newInstance(),"Videos");
        sectionsPagerAdapter.addFragment(TheoryResourceFragment.newInstance(),"Theories");
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