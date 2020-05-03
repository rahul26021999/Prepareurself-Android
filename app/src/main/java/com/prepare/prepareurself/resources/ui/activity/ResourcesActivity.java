package com.prepare.prepareurself.resources.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.resources.ui.fragments.TheoryResourceFragment;
import com.prepare.prepareurself.resources.ui.fragments.VideoResourceFragment;
import com.prepare.prepareurself.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.resources.viewmodel.ResourceViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ResourcesActivity extends BaseActivity implements View.OnClickListener, TheoryResourceFragment.TheoryResourceFragmentInteractor {

    private ViewPager viewPager;
    private ResourceViewModel viewModel;

    private PrefManager prefManager;

    private RelativeLayout relVideo, relTheory;
    private TextView tvTopVideo, tvTopTheory,title;
    private ImageView BackBtn;
    public static int topicID;
    public int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        viewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        viewPager = findViewById(R.id.view_pager_resources);
        tvTopVideo = findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = findViewById(R.id.tv_resouce_heading_theory);
        title=findViewById(R.id.title);
        BackBtn=findViewById(R.id.backBtn);
        Intent intent = getIntent();
        title.setText("Resources");
        prefManager = new PrefManager(ResourcesActivity.this);

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(VideoResourceFragment.newInstance(),"Videos");
        sectionsPagerAdapter.addFragment(TheoryResourceFragment.newInstance(),"Theories");
        viewPager.setAdapter(sectionsPagerAdapter);



        if (intent.getData()!=null){
            Log.d("deeplink_debug","resource activity "+intent.getData()+"");

            String data = intent.getData().toString();
//            String data = "com.prepare.prepareurself.resources.ui.activity.ResourcesActivity?type=video&topic_id=1&topic_name=getting started&course_name=Android";


            if (data.contains("type")){
                String CourseName = data.split("&course_name=")[1];
                String topic_name = data.split("&topic_name=")[1].split("&course_name=")[0];
                String topic_id = data.split("&topic_id=")[1].split("&topic_name=")[0];
                String type = data.split("type=")[1].split("&topic_id=")[0];


                title.setText(CourseName);
                topicID = Integer.parseInt(topic_id);

                if (type.equals("video")){
                    viewPager.setCurrentItem(0);
                }else{
                    viewPager.setCurrentItem(1);
                }
            }else if (data.contains("link")){
                viewPager.setCurrentItem(1);
                String url = intent.getData().toString().split("&link=")[1];
                Utility.redirectUsingCustomTab(this,url);
                finish();
            }

        }else{
            topicID = intent.getIntExtra(Constants.TOPICID,-1);
            courseId = intent.getIntExtra(Constants.COURSEID, -1);
        }

        BackBtn.setOnClickListener(this);
        tvTopVideo.setOnClickListener(this);
        tvTopTheory.setOnClickListener(this);

        if (courseId!=-1){
            viewModel.getCourseById(courseId)
                    .observe(ResourcesActivity.this, new Observer<CourseModel>() {
                        @Override
                        public void onChanged(CourseModel courseModel) {
                            if (courseModel!=null){
                                title.setText(courseModel.getName());
                            }else{
                                title.setText("Resource");
                            }
                        }
                    });
        }else{
            title.setText("Resource");
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.dark_grey));
                }else if (position == 1){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.dark_grey));
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
        try {
            Uri bitmapUri = Utility.getUriOfBitmap(bitmap, this);
            Utility.shareContent(this,bitmapUri,text);
        } catch (IOException e) {
            e.printStackTrace();
        }

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