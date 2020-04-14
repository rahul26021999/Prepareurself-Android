package com.example.prepareurself.Home.content.resources.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.prepareurself.Home.content.resources.ui.fragments.TheoryResourceFragment;
import com.example.prepareurself.Home.content.resources.ui.fragments.VideoResourceFragment;
import com.example.prepareurself.Home.content.resources.ui.adapter.SectionsPagerAdapter;
import com.example.prepareurself.Home.content.resources.viewmodel.ResourceViewModel;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.PrefManager;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private ResourceViewModel viewModel;

    private PrefManager prefManager;

    public static int topicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        viewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        viewPager = findViewById(R.id.view_pager_resources);
        tabs = findViewById(R.id.tabs_topics);

        Intent intent = getIntent();
        topicID = intent.getIntExtra(Constants.TOPICID,-1);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Resources");

        prefManager = new PrefManager(ResourcesActivity.this);

        if (topicID!=-1){
            viewModel.fetchResources(prefManager.getString(Constants.JWTTOKEN),topicID);
        }

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