package com.prepare.prepareurself.Home.content.resources.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prepare.prepareurself.Home.content.resources.ui.fragments.TheoryResourceFragment;
import com.prepare.prepareurself.Home.content.resources.ui.fragments.VideoResourceFragment;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.Home.content.resources.viewmodel.ResourceViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

public class ResourcesActivity extends AppCompatActivity implements View.OnClickListener, TheoryResourceFragment.TheoryResourceFragmentInteractor {

    private ViewPager viewPager;
    private ResourceViewModel viewModel;

    private PrefManager prefManager;

    private RelativeLayout relVideo, relTheory;
    private TextView tvTopVideo, tvTopTheory;
    private ImageView BackBtn;
    public static int topicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        viewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        viewPager = findViewById(R.id.view_pager_resources);
        tvTopVideo = findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = findViewById(R.id.tv_resouce_heading_theory);
        BackBtn=findViewById(R.id.backBtn);
        Intent intent = getIntent();


        prefManager = new PrefManager(ResourcesActivity.this);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(VideoResourceFragment.newInstance(),"Videos");
        sectionsPagerAdapter.addFragment(TheoryResourceFragment.newInstance(),"Theories");
        viewPager.setAdapter(sectionsPagerAdapter);

        if (intent.getData()!=null){
            try {
                String tempData = intent.getData().toString().split("resource_theory")[1];
                topicID =  Utility.base64Decode(tempData);
                viewPager.setCurrentItem(1, true);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            topicID = intent.getIntExtra(Constants.TOPICID,-1);
        }

        BackBtn.setOnClickListener(this);
        tvTopVideo.setOnClickListener(this);
        tvTopTheory.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.lightgrey));
                }else if (position == 1){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.lightgrey));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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

    @Override
    public void shareContent(Bitmap bitmap, String text) {
        Utility.shareContent(this,bitmap,text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_resouce_heading_theory:
                    viewPager.setCurrentItem(1, true);
                    break;
            case R.id.tv_resouce_heading_video:
                viewPager.setCurrentItem(0, true);
                break;
        }
    }
}