package com.prepare.prepareurself.Home.content.resources.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prepare.prepareurself.Home.content.resources.ui.fragments.TheoryResourceFragment;
import com.prepare.prepareurself.Home.content.resources.ui.fragments.VideoResourceFragment;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.Home.content.resources.viewmodel.ResourceViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private ResourceViewModel viewModel;

    private PrefManager prefManager;

    private RelativeLayout relVideo, relTheory;
    private TextView tvTopVideo, tvTopTheory;

    public static int topicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        viewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        viewPager = findViewById(R.id.view_pager_resources);
        tabs = findViewById(R.id.tabs_topics);
//        relVideo = findViewById(R.id.rel_resource_videos);
//        relTheory = findViewById(R.id.rel_resource_theory);
        tvTopVideo = findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = findViewById(R.id.tv_resouce_heading_theory);

        Intent intent = getIntent();
        topicID = intent.getIntExtra(Constants.TOPICID,-1);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        getSupportActionBar().setTitle("Resources");

        prefManager = new PrefManager(ResourcesActivity.this);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(VideoResourceFragment.newInstance(),"Videos");
        sectionsPagerAdapter.addFragment(TheoryResourceFragment.newInstance(),"Theories");
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        tvTopVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });

        tvTopTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.black));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.dark_grey));
                }else if (position == 1){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.dark_grey));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



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