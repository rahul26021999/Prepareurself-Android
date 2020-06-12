package com.prepare.prepareurself.courses.ui.activity;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.AddToUserPrefResponseModel;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabProjectctivity;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabResourceActivity;
import com.prepare.prepareurself.courses.viewmodels.CourseDetailViewModel;
import com.prepare.prepareurself.feedback.ui.FeedbackFragment;
import com.prepare.prepareurself.quizv2.ui.QuizActivity;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.willy.ratingbar.BaseRatingBar;

import java.io.IOException;

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener{
    int starbyuser;
    String msg;
    RateCourseResponseModel rateCourseResponseModel;

    private ImageView backBtn ,course_image, btn_shareimage, pref_image;
    private CourseDetailViewModel vm;
    private int courseId = -1;
    private PrefManager prefManager;
    private com.willy.ratingbar.ScaleRatingBar scaleRatingBar;
    //private Button btnProject, btnResources;
    private LinearLayout l_layout_pref;
    private RelativeLayout rel_project, rel_resources, rel_forum, rel_takequiz;
    TextView course_name, course_description,tv_pref_name;
    //private FeedbackFragment feedbackFragment;
    //CardView cd_forum, cd_takequiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        course_name=findViewById(R.id.coursename);
        course_description=findViewById(R.id.course_desc);
        course_image=findViewById(R.id.course_image);
        backBtn=findViewById(R.id.backBtn);
        l_layout_pref=findViewById(R.id.l_layout_pref);
        tv_pref_name=findViewById(R.id.tv_pref_name);
        pref_image=findViewById(R.id.pref_image);
        scaleRatingBar=findViewById(R.id.scaleRatingBar);
        rel_project=findViewById(R.id.rel_project);
        rel_resources=findViewById(R.id.rel_resources);
        rel_takequiz=findViewById(R.id.rel_takequiz);
        btn_shareimage=findViewById(R.id.btn_shareimage);
        rel_forum=findViewById(R.id.rel_forum);
        backBtn.setOnClickListener(this);
        rel_project.setOnClickListener(this);
        rel_resources.setOnClickListener(this);
        rel_takequiz.setOnClickListener(this);
        rel_forum.setOnClickListener(this);
        btn_shareimage.setOnClickListener(this);
        l_layout_pref.setOnClickListener(this);
        //int stars=scaleRatingBar.getNumStars();
        //Log.d("TAG",""+stars);
        scaleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("TAGSTAR", "onRatingChange: " + starbyuser);
                starbyuser= (int) rating;
                Toast.makeText(CourseDetailActivity.this,""+ msg,Toast.LENGTH_LONG).show();
                if(rating<=3){
                    showDialogfeedback();
                }
                else if (rating>3){
                    showDialogplaystore();
                }
            }
        });

        vm = new  ViewModelProvider(this).get(CourseDetailViewModel.class);
        prefManager = new PrefManager(this);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(Constants.COURSEID, -1);

        if (courseId!=-1){
            vm.fetchCourseDetails(prefManager.getString(Constants.JWTTOKEN),courseId);
            vm.fetchRateCourse(prefManager.getString(Constants.JWTTOKEN),courseId,starbyuser);
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
                   // Toast.makeText(CourseDetailActivity.this,""+ rateCourseResponseModel.getMessage(),Toast.LENGTH_LONG).show();
                         msg=rateCourseResponseModel.getMessage();

                }
            });
            //Adduser pref model
            vm.addToUserPrefResponseModelLiveData.observe(this, new Observer<AddToUserPrefResponseModel>() {
                @Override
                public void onChanged(AddToUserPrefResponseModel addToUserPrefResponseModel) {
                    Toast.makeText(CourseDetailActivity.this,""+addToUserPrefResponseModel.getMessage(),Toast
                    .LENGTH_LONG).show();
                }
            });

        }

    }

    private void showDialogplaystore() {
        final Dialog dialog = new Dialog(CourseDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_dialog_playstore);
        com.google.android.material.button.MaterialButton buttonok =dialog.findViewById(R.id.btn_ok);
        com.google.android.material.button.MaterialButton buttonnotnow =dialog.findViewById(R.id.btn_notnow);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.redirectUsingCustomTab(CourseDetailActivity.this,Constants.GOOGLEPLAYLINK);
                dialog.dismiss();

            }
        });
        buttonnotnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
    }


    private void showDialogfeedback() {
        final Dialog dialog = new Dialog(CourseDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_dialog);
        com.google.android.material.button.MaterialButton buttonok =dialog.findViewById(R.id.btn_ok);
        com.google.android.material.button.MaterialButton buttonnotnow =dialog.findViewById(R.id.btn_notnow);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                /*Intent intent = new Intent(CourseDetailActivity.this, FeedbackFragment.class);
                startActivity(intent);*/
                //MyFragment fragment = new MyFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.rel_frag_layout, FeedbackFragment);
                transaction.commit();
            }
        });
        buttonnotnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(CourseDetailActivity.this, FeedbackFragment.class);
                startActivity(intent);
            }
        });
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

/*

        FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourseDetailActivity.this,"Cancel",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourseDetailActivity.this,"Okay" ,Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
*/

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                //startActivity(new Intent(CourseDetailActivity.this, HomeActivity.class));
                finish();
                break;
            case R.id.rel_project:
                Intent intent = new Intent(CourseDetailActivity.this, TabProjectctivity.class);
                intent.putExtra(Constants.COURSEID,1);
                intent.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent);
                break;
            case R.id.rel_resources:
                Intent intent1 = new Intent(CourseDetailActivity.this, TabResourceActivity.class);
                intent1.putExtra(Constants.COURSEID,1);
                intent1.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent1);
                break;
            case R.id.rel_takequiz:
                Intent intent2 = new Intent(CourseDetailActivity.this, QuizActivity.class);
                intent2.putExtra(Constants.COURSEID,courseId);
                startActivity(intent2);
                break;
            /*case R.id.tv_setpref:
                Intent intent3 = new Intent(CourseDetailActivity.this, PreferencesActivity.class);
                startActivity(intent3);
                break;*/
            case R.id.btn_shareimage:
                try{

                Uri uri = Utility.getUriOfBitmap(Utility.getBitmapFromView(course_image),CourseDetailActivity.this);
                String encodedId = Utility.base64EncodeForInt(courseId);
                String text = course_name+"\n\n" +
                        "Prepareurself is providing various courses, projects and resources. " +
                        "One place to learn skills and test them by developing projects. \n" +
                        "Checkout prepareurself app : \n" +
                        "prepareurself.in/project/"+encodedId;
                Utility.shareContent(CourseDetailActivity.this,uri,text);
                }
                 catch (IOException e) {
                         e.printStackTrace();
                }
                break;
            case R.id.rel_forum:
                Intent intent4 = new Intent(CourseDetailActivity.this, ForumActivity.class);
                startActivity(intent4);
                break;
            case R.id.l_layout_pref:
                Glide.with(CourseDetailActivity.this).load(R.drawable.ic_check_black_24dp).into(pref_image);
                //image
                tv_pref_name.setText("Added as preference");

    }
}
}