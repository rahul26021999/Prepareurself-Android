package com.prepare.prepareurself.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prepare.prepareurself.courses.ui.fragments.ProjectsFragment;
import com.prepare.prepareurself.courses.ui.fragments.ResourcesFragment;
import com.prepare.prepareurself.courses.viewmodels.TopicViewModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.google.android.material.tabs.TabLayout;

public class CoursesActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabs;
    private ImageView backBtn;
    private TextView title;

    public static int courseId;

    private TopicViewModel topicViewModel;

    private PrefManager prefManager;
    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        topicViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        viewPager = findViewById(R.id.view_pager_courses);
        tabs = findViewById(R.id.tabs_courses);
        title=findViewById(R.id.title);
        backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);

        prefManager = new PrefManager(CoursesActivity.this);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(ProjectsFragment.newInstance(),"Projects");
        sectionsPagerAdapter.addFragment(ResourcesFragment.newInstance(),"Resources");
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);


        Intent intent = getIntent();

        if (intent.getData()!=null){
            //com.prepare.prepareurself.courses.ui.activity.CoursesActivity?type=Projects&course_id=1&course_name=Android
            Log.d("deeplink_debug","course avtivity : "+getIntent().getData()+"");
            String data = intent.getData().toString();
            String CourseName = data.split("&course_name=")[1];
            String CourseId = data.split("&course_id=")[1].split("&course_name=")[0];
            String type = data.split("type=")[1].split("&course_id=")[0];

            courseId = Integer.parseInt(CourseId);
            courseName = CourseName;

//            if (!TextUtils.isEmpty(courseName)){
//                title.setText(courseName);
//            }else{
//                title.setText("Tech Stack");
//            }

            if(type.equals("Projects")){
                viewPager.setCurrentItem(0);
            }else if (type.equals("Topics")){
                viewPager.setCurrentItem(1);
            }

            Log.d("deeplink_debug",courseName+","+courseId+","+type);
        }else{
            courseId = intent.getIntExtra(Constants.COURSEID, -1);
            courseName = intent.getStringExtra(Constants.COURSENAME);
//            if (!TextUtils.isEmpty(courseName)){
//                title.setText(courseName);//Course Name Here
//            }else{
//                topicViewModel.getCourseModelById(courseId).observe(this, new Observer<CourseModel>() {
//                    @Override
//                    public void onChanged(CourseModel courseModel) {
//                        if (courseModel!=null){
//                            title.setText(courseModel.getName());
//                        }else{
//                            title.setText("Tech-Stack");
//                        }
//                    }
//                });
//            }
            if (intent.getStringExtra(Constants.SHOWPAGE)!=null){
                String showPage = intent.getStringExtra(Constants.SHOWPAGE);
                Log.d("se_all_debug",showPage+"");
                if (showPage!=null){
                    if(showPage.equals(Constants.SHOWPROJECTS)){
                        viewPager.setCurrentItem(0);
                    }else if (showPage.equals(Constants.SHOWTOPICS)){
                        viewPager.setCurrentItem(1);
                    }
                }

            }else{
                viewPager.setCurrentItem(0);
            }

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position ==0){
                    title.setText("Projects");
                }else if (position ==1){
                    title.setText("Resources");
                }
            }

            @Override
            public void onPageSelected(int position) {
//                if (position ==0){
//                    title.setText("Projects");
//                }else if (position ==1){
//                    title.setText("Resources");
//                }
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
