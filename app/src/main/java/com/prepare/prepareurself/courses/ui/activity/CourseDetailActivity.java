package com.prepare.prepareurself.courses.ui.activity;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.chatbot.ui.ChatBotActivity;
import com.prepare.prepareurself.courses.data.model.AddToUserPrefResponseModel;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabProjectctivity;
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabResourceActivity;
import com.prepare.prepareurself.courses.viewmodels.CourseDetailViewModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.feedback.ui.FeedbackFragment;
import com.prepare.prepareurself.forum.ui.ForumActivity;
import com.prepare.prepareurself.preferences.data.PreferencesModel;
import com.prepare.prepareurself.preferences.data.PrefernceResponseModel;
import com.prepare.prepareurself.quizv2.ui.QuizDetailActivity;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.willy.ratingbar.BaseRatingBar;

import java.io.IOException;
import java.util.List;

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener,
        FeedbackFragment.FeedBackHomeInteractor,
        BaseRatingBar.OnRatingChangeListener {
    int starbyuser,type=0;
    String msg;
    private int courseId = -1;
    private ImageView backBtn ,course_image, btn_shareimage, pref_image;
    private CourseDetailViewModel vm;
    private PrefManager prefManager;
    private com.willy.ratingbar.ScaleRatingBar scaleRatingBar;
    private LinearLayout l_layout_pref , topCourseBackground,belowCourseBackgroundContainer;
    private RelativeLayout rel_project, rel_resources, rel_forum, rel_takequiz,mainBackground;
    TextView course_name, course_description,tv_pref_name,averageRating,count_topics,count_projects;
    ImageView forumIcon,ProjectIcon,ResourceIcon,QuizIcon;
    private String courseName = "";
    private boolean isAdded = false;
    private boolean isRateFetching = true;
    private Dialog playstoreDialog;
    private Dialog feedbackDialog;
    private FeedbackFragment feedbackFragment;
    private ShimmerFrameLayout mShimmerViewContainer;
    private NestedScrollView relTop;
    private ExtendedFloatingActionButton botFab;
    private String gradColor = "";


    @Override
    public void onFeedbackBackPressed() { }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        vm = new  ViewModelProvider(this).get(CourseDetailViewModel.class);
        prefManager = new PrefManager(this);
        final Intent intent = getIntent();
        feedbackFragment = FeedbackFragment.newInstance();
        if (intent.getData()!=null){
            Log.d("deeplink_debug","course avtivity : "+getIntent().getData()+"");
            String data = intent.getData().toString();
            String CourseName = data.split("&course_name=")[1];
            String CourseId = data.split("&course_id=")[1].split("&course_name=")[0];
            String type = data.split("type=")[1].split("&course_id=")[0];
            courseId = Integer.parseInt(CourseId);
        }else{
            courseId = intent.getIntExtra(Constants.COURSEID, -1);
        }
        intialiseVariables();
        setOnclicklisteners();

        initFeedbackDialog();
        initPlayStoreDialog();

        if (courseId!=-1){
            vm.fetchCourseDetails(prefManager.getString(Constants.JWTTOKEN),courseId);
            vm.fetchremotePref(prefManager.getString(Constants.JWTTOKEN));

            vm.courseDetailReponseModelLiveData.observe(this, new Observer<CourseDetailReponseModel>() {
                @Override
                public void onChanged(CourseDetailReponseModel courseDetailReponseModel) {

                    if (courseDetailReponseModel!=null ){

                        relTop.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        final CourseModel course=courseDetailReponseModel.getCourse();
                        gradColor = course.getColor();
                        setbggradientdynamicupperlayout(gradColor);

                        String title=course.getName();
                        String description=course.getDescription();
                        String logo_url=course.getLogo_url();
                        float rating=course.getAverage_rating();
                        String f= String.format("%.1f", rating);
                        averageRating.setText(f);
                        int topic_count=courseDetailReponseModel.getTopic_count();
                        int project_count=courseDetailReponseModel.getProject_count();

                        Log.d("count",""+topic_count);

                       if(topic_count>0){
                           rel_resources.setVisibility(View.VISIBLE);
                           count_topics.setText(""+topic_count);
                       }

                       if(project_count>0){
                            rel_project.setVisibility(View.VISIBLE);
                            count_projects.setText(""+project_count);

                       }
                        Log.d("count",""+project_count);
                        course_name.setText(title);
                        courseName = title;
                        if (description!=null)
                            course_description.setText(Html.fromHtml(description).toString().trim());
                        scaleRatingBar.setRating(courseDetailReponseModel.getRating());
                        if (courseDetailReponseModel.getRating() == 0){
                            isRateFetching = false;
                        }
                        Log.d("rating_debug","rating : "+ courseDetailReponseModel.getRating());
                        if (logo_url!=null && logo_url.endsWith(".svg")){
                            Utility.loadSVGImage(CourseDetailActivity.this, course.getLogo_url(),course_image);
                        }else{
                            Glide.with(CourseDetailActivity.this).load(course.getLogo_url())
                                    .placeholder(R.drawable.placeholder)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .into(course_image);
                        }
                    }else{
                        Utility.showToast(CourseDetailActivity.this,Constants.SOMETHINGWENTWRONG);
                    }
                }
            });
            botFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(CourseDetailActivity.this, ChatBotActivity.class);
                    intent1.putExtra(Constants.COURSENAME,courseName);
                    intent1.putExtra("gradColor",gradColor);
                    startActivity(intent1);
                }
            });
            //rate course
            vm.rateCourseResponseModelLiveData.observe(this, new Observer<RateCourseResponseModel>() {
                @Override
                public void onChanged(RateCourseResponseModel rateCourseResponseModel) {
                    if(rateCourseResponseModel!=null)
                        msg=rateCourseResponseModel.getMessage();

                }
            });
            //fetch user remote lst
            vm.prefernceResponseModelLiveData.observe(this, new Observer<PrefernceResponseModel>() {
                int l_size;
                @Override
                public void onChanged(PrefernceResponseModel prefernceResponseModel) {

                    if (prefernceResponseModel!=null){
                        List<PreferencesModel> preferencesModelslist =prefernceResponseModel.getPreferences();
                        if (preferencesModelslist!=null && !(preferencesModelslist.isEmpty())){
                            l_size=preferencesModelslist.size();
                            Log.d("TAGSZE",""+l_size);
                            for (int j=0;j<l_size;j++){
                                if(courseId==preferencesModelslist.get(j).getId()){
                                    Glide.with(CourseDetailActivity.this).load(R.drawable.ic_check_black_24dp).into(pref_image);
                                    tv_pref_name.setText("Added as preference");
                                    isAdded = true;
                                    break; }
                            }
                        }
                    }else{
                        Utility.showToast(CourseDetailActivity.this,Constants.SOMETHINGWENTWRONG);
                    }



                }
            });

            scaleRatingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP){
                        Log.d("rate bar", "onTouch: action up");
                       setRatingListener();
                    }
                    return false;
                }
            });
        }

    }

    private void setRatingListener() {
        scaleRatingBar.setOnRatingChangeListener(this);
    }

    @Override
    public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
        starbyuser= (int) rating;
        Log.e("TAGSTAR", "onRatingChange: " + isRateFetching);
        if (!isRateFetching){
            vm.fetchRateCourse(prefManager.getString(Constants.JWTTOKEN),courseId,starbyuser);
            observeData(starbyuser);
        }else{
            isRateFetching = false;
        }
    }

    private void observeData(final int starbyuser) {
        vm.rateCourseResponseModelLiveData.observe(this, new Observer<RateCourseResponseModel>() {
            @Override
            public void onChanged(RateCourseResponseModel rateCourseResponseModel) {
                if (rateCourseResponseModel!=null){
                    Log.d("star_debug", "onRatingChange: "+starbyuser);
                    if(starbyuser<=3){
                        feedbackDialog.show();
                        if (playstoreDialog.isShowing()){
                            playstoreDialog.cancel();
                        }
                    }
                    else {
                       playstoreDialog.show();
                       if (feedbackDialog.isShowing()){
                           feedbackDialog.cancel();
                       }
                    }
                }
            }
        });
    }

    private void setOnclicklisteners() {
        backBtn.setOnClickListener(this);
        rel_project.setOnClickListener(this);
        rel_resources.setOnClickListener(this);
        rel_takequiz.setOnClickListener(this);
        rel_forum.setOnClickListener(this);
        btn_shareimage.setOnClickListener(this);
        l_layout_pref.setOnClickListener(this);
    }

    private void setbggradientdynamicupperlayout(String gradColor)
    {
        if(!gradColor.equals(""))
        {
            String[] list = gradColor.split(",");
            Log.i("Colors",gradColor+list.length);
            int[] colors = new int[list.length];
            for (int i=0;i<list.length;i++){
                colors[i]=Color.parseColor(list[i]);
            }
            GradientDrawable myGradBg = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
            myGradBg.setCornerRadii(new float[]{0f,0f,0f,0f,90f,90f,0f,0f});
            topCourseBackground.setBackground(myGradBg);
            mainBackground.setBackground(myGradBg);
            belowCourseBackgroundContainer.setBackground(myGradBg);

            botFab.setBackgroundColor(colors[0]);

            GradientDrawable icongrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
            icongrad.setCornerRadius(20f);
            forumIcon.setBackground(icongrad);
            ProjectIcon.setBackground(icongrad);
            ResourceIcon.setBackground(icongrad);
            QuizIcon.setBackground(icongrad);

        }

    }

    private void intialiseVariables() {

        botFab = findViewById(R.id.bot_fab);
        relTop = findViewById(R.id.rel_layoutmain1);
        mShimmerViewContainer = findViewById(R.id.course_shimmer_view_container);
        course_name=findViewById(R.id.coursename);
        course_description=findViewById(R.id.course_desc);
        course_image=findViewById(R.id.course_image);
        backBtn=findViewById(R.id.backBtn);
        averageRating=findViewById(R.id.averageRating);
        mainBackground=findViewById(R.id.mainBackground);
        l_layout_pref=findViewById(R.id.l_layout_pref);
        belowCourseBackgroundContainer=findViewById(R.id.belowCourseBackgroundContainer);
        topCourseBackground =findViewById(R.id.topCourseBackground);
        tv_pref_name=findViewById(R.id.tv_pref_name);
        count_topics=findViewById(R.id.count_topics);
        count_projects=findViewById(R.id.count_projects);
        pref_image=findViewById(R.id.pref_image);
        scaleRatingBar=findViewById(R.id.scaleRatingBar);
        rel_project=findViewById(R.id.rel_project);
        rel_resources=findViewById(R.id.rel_resources);
        rel_takequiz=findViewById(R.id.rel_takequiz);
        btn_shareimage=findViewById(R.id.btn_shareimage);
        rel_forum=findViewById(R.id.rel_forum);
        forumIcon=findViewById(R.id.forumIcon);
        ProjectIcon=findViewById(R.id.projectIcon);
        QuizIcon=findViewById(R.id.quizIcon);
        ResourceIcon=findViewById(R.id.resourceIcon);

    }


    private void initPlayStoreDialog(){
        playstoreDialog = new Dialog(CourseDetailActivity.this);
        playstoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        playstoreDialog.setCancelable(false);
        //View view = LayoutInflater.from(this).inflate(R.layout.activity_dialog_playstore, null);
        playstoreDialog.setContentView(R.layout.activity_dialog_playstore);
        com.google.android.material.button.MaterialButton buttonok =playstoreDialog.findViewById(R.id.btn_ok);
        TextView buttonnotnow =playstoreDialog.findViewById(R.id.btn_notnow);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.redirectUsingCustomTab(CourseDetailActivity.this,Constants.GOOGLEPLAYLINK);
                playstoreDialog.dismiss();
            }
        });
        buttonnotnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playstoreDialog.cancel();
            }
        });
        playstoreDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        playstoreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


    private void initFeedbackDialog() {
        feedbackDialog = new Dialog(CourseDetailActivity.this);
        feedbackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedbackDialog.setCancelable(false);
        feedbackDialog.setContentView(R.layout.activity_dialog);
        com.google.android.material.button.MaterialButton buttonok =feedbackDialog.findViewById(R.id.btn_ok);
        TextView buttonnotnow =feedbackDialog.findViewById(R.id.btn_notnow);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackDialog.cancel();
                findViewById(R.id.feedback_container).setVisibility(View.VISIBLE);
                feedbackFragment.fromCourse = true;
                getSupportFragmentManager().beginTransaction().add(R.id.feedback_container,feedbackFragment)
                        .commit();
               // Utility.showToast(CourseDetailActivity.this,"abc def");
            }
        });
        buttonnotnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackDialog.cancel();
            }
        });
       feedbackDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
       feedbackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                intent.putExtra(Constants.COURSEID,courseId);
                startActivity(intent);
                break;
            case R.id.rel_resources:
                Intent intent1 = new Intent(CourseDetailActivity.this, TabResourceActivity.class);
                intent1.putExtra(Constants.COURSEID,courseId);
                //intent1.putExtra(Constants.COURSENAME,"Android");
                startActivity(intent1);
                break;
            case R.id.rel_takequiz:
                //Intent intent2 = new Intent(CourseDetailActivity.this, QuizActivity.class);
                Intent intent2 = new Intent(CourseDetailActivity.this, QuizDetailActivity.class);
                //intent2.putExtra(Constants.COURSEID,courseId);
                //intent2.putExtra(Constants.COURSENAME,courseName);
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
                String text = "Prepareurself is providing various courses, projects and resources. " +
                        "One place to learn skills and test them by developing projects. \n" +
                        "Checkout prepareurself app : \n" +
                        "prepareurself.in/course/"+encodedId;
                Utility.shareContent(CourseDetailActivity.this,uri,text);
                }
                 catch (IOException e) {
                         e.printStackTrace();
                }
                break;
            case R.id.rel_forum:
                Intent intent4 = new Intent(CourseDetailActivity.this, ForumActivity.class);
                intent4.putExtra(Constants.COURSEID,courseId);
                intent4.putExtra(Constants.COURSENAME,courseName);
                intent4.putExtra("gradColor",gradColor);
                startActivity(intent4);
                break;
            case R.id.l_layout_pref:
                if (!isAdded){
                    type=1;
                    vm.fetchAddUserPref(prefManager.getString(Constants.JWTTOKEN),courseId,type);
                    vm.addToUserPrefResponseModelLiveData.observe(this, new Observer<AddToUserPrefResponseModel>() {
                        @Override
                        public void onChanged(AddToUserPrefResponseModel addToUserPrefResponseModel) {

                            if (addToUserPrefResponseModel!=null){
                                Utility.showToast(CourseDetailActivity.this,addToUserPrefResponseModel.getMessage());
                                Glide.with(CourseDetailActivity.this).load(R.drawable.ic_check_black_24dp).into(pref_image);
                                tv_pref_name.setText("Added as preference");
                            }else{
                                Utility.showToast(CourseDetailActivity.this, Constants.SOMETHINGWENTWRONG);
                            }
                        }
                    });
                }
                break;
    }
}

    @Override
    protected void onResume() {
        super.onResume();
//        findViewById(R.id.feedback_container).setVisibility(View.GONE);
//        relTop.setVisibility(View.GONE);
//        mShimmerViewContainer.startShimmerAnimation();
      //  setbggradientdynamicupperlayout(gradColor);
    }

    @Override
    protected void onPause() {
//        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        if (feedbackFragment.isVisible()){
            feedbackFragment.removeFragment();
        }else{
            super.onBackPressed();
        }
    }
}
