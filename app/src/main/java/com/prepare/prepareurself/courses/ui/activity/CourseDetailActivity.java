package com.prepare.prepareurself.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabProjectctivity;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabResourceActivity;
import com.prepare.prepareurself.courses.viewmodels.CourseDetailViewModel;
import com.prepare.prepareurself.preferences.ui.PreferencesActivity;
import com.prepare.prepareurself.profile.ui.EditPreferenceActivity;
import com.prepare.prepareurself.quizv2.ui.QuizActivity;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import okio.Utf8;

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView backBtn ,course_image;
    private CourseDetailViewModel vm;
    private int courseId = -1;
    private PrefManager prefManager;
    private RatingBar rateCourseBar;
    private Button btnProject, btnResources, btnShareCourse;
    TextView course_name, course_description, tv_takequiz, tv_setpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        course_name=findViewById(R.id.coursename);
        course_description=findViewById(R.id.course_desc);
        course_image=findViewById(R.id.course_image);
        backBtn=findViewById(R.id.backBtn);
        rateCourseBar=findViewById(R.id.rateCourseBar);
        btnProject=findViewById(R.id.btnProject);
        btnResources=findViewById(R.id.btnResources);
        tv_takequiz=findViewById(R.id.tv_takequiz);
        tv_setpref=findViewById(R.id.tv_setpref);
        backBtn.setOnClickListener(this);
        btnProject.setOnClickListener(this);
        btnResources.setOnClickListener(this);
        tv_takequiz.setOnClickListener(this);
        tv_setpref.setOnClickListener(this);
        vm = new  ViewModelProvider(this).get(CourseDetailViewModel.class);
        prefManager = new PrefManager(this);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(Constants.COURSEID, -1);

        if (courseId!=-1){
            vm.fetchCourseDetails(prefManager.getString(Constants.JWTTOKEN),courseId);
            vm.fetchRateCourse(prefManager.getString(Constants.JWTTOKEN),courseId,5);
            vm.courseDetailReponseModelLiveData.observe(this, new Observer<CourseDetailReponseModel>() {
                @Override
                public void onChanged(CourseDetailReponseModel courseDetailReponseModel) {
                    if (courseDetailReponseModel!=null){
                        String title=courseDetailReponseModel.getCourse().getName();
                        String description=courseDetailReponseModel.getCourse().getDescription();
                        String img_url=courseDetailReponseModel.getCourse().getImage_url();
                        //setdetails();
                        course_name.setText(title);
                        Log.d("TAGdeatal",title);
                        course_description.setText(description);
                        if (img_url!=null && img_url.endsWith(".svg")){
                            Utility.loadSVGImage(CourseDetailActivity.this,Constants.COURSEIMAGEBASEUSRL+ courseDetailReponseModel.getCourse().getImage_url(),course_image);
                        }else{
                            Glide.with(CourseDetailActivity.this).load(
                                    Constants.COURSEIMAGEBASEUSRL+ courseDetailReponseModel.getCourse().getImage_url())
                                    .placeholder(R.drawable.placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                    .into(course_image);
                        }


                        
                    }else{
                        Utility.showToast(CourseDetailActivity.this,Constants.SOMETHINGWENTWRONG);
                    }
                }
            });
            //
            vm.rateCourseResponseModelLiveData.observe(this, new Observer<RateCourseResponseModel>() {

                @Override
                public void onChanged(RateCourseResponseModel rateCourseResponseModel) {
                    Toast.makeText(CourseDetailActivity.this,""+ rateCourseResponseModel.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }

    }



    private void setdetails() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                //startActivity(new Intent(CourseDetailActivity.this, HomeActivity.class));
                finish();
                break;
            case R.id.btnProject:
                Intent intent = new Intent(CourseDetailActivity.this, TabProjectctivity.class);
                intent.putExtra(Constants.COURSEID,1);
                intent.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent);
                break;
            case R.id.btnResources:
                Intent intent1 = new Intent(CourseDetailActivity.this, TabResourceActivity.class);
                intent1.putExtra(Constants.COURSEID,1);
                intent1.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent1);
                break;
            case R.id.tv_takequiz:
                Intent intent2 = new Intent(CourseDetailActivity.this, QuizActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_setpref:
                Intent intent3 = new Intent(CourseDetailActivity.this, PreferencesActivity.class);
                startActivity(intent3);
                break;

        }
    }
}
