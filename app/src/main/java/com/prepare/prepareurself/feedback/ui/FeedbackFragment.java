package com.prepare.prepareurself.feedback.ui;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.dashboard.ui.adapters.CustomPagerAdapter;
import com.prepare.prepareurself.feedback.data.model.FeedbackFourOptionsModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackInoutModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackParentModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackTwoOptionsModel;
import com.prepare.prepareurself.feedback.data.model.FeedbacksubmitModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFragment extends Fragment {

    FeedbackViewModel mViewModel;

    private LinearLayout linUserInput, linTwoOptions, linFourOptions;
    private ImageView verygood_tick_2, good_tick_2;
    private CardView verygood_cd_2 , good_cd_2;
    private TextView tvOption1_2, tvOption2_2, tvQuestion_2;

    private ImageView verygood_tick, good_tick, fair_tick, bad_tick,imageView1,imageView2,imageView3,imageView4;
    private CardView verygood_cd , good_cd, fair_cd, bad_cd;
    private TextView tvOption1, tvOption2, tvOption3, tvOption4, tvQuestion;

    private ImageView backBtn;
    private TextView tvQuestion_3;
    private EditText editText;

    private Button nextButton;

    private int currentPosition = 0;
    String tempAnswer = "";

    List<String> answers = new ArrayList<>();

    private FeedbackParentModel feedbackParentModel;
    private PrefManager prefManager;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.feedback_fragment_2, container, false);
       // viewPager = v.findViewById(R.id.feedback_pager);
        nextButton = v.findViewById(R.id.btn_next);
        backBtn=v.findViewById(R.id.backBtn);
        bindFourOptionsLayout(v);
        binTwoOptionsLayout(v);
        bindUserInputOptionsLayout(v);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);

        final List<FeedbackParentModel> feedbackParentModels = mViewModel.getFeedbackParent();

        prefManager = new PrefManager(getActivity());


//        customPagerAdapter = new CustomPagerAdapter(getActivity(), feedbackParentModels, this);
//        viewPager.setAdapter(customPagerAdapter);

        currentPosition = 0;
        feedbackParentModel = feedbackParentModels.get(currentPosition);
        setView(feedbackParentModel);

        verygood_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.VISIBLE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
                tempAnswer = feedbackParentModel.getFeedbackFourOptionsModel().getOptions().get(0);
            }
        });
        good_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.VISIBLE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
                tempAnswer = feedbackParentModel.getFeedbackFourOptionsModel().getOptions().get(1);
            }
        });
        fair_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.VISIBLE);
                bad_tick.setVisibility(View.GONE);

                tempAnswer = feedbackParentModel.getFeedbackFourOptionsModel().getOptions().get(2);

            }
        });
        bad_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.VISIBLE);

                tempAnswer = feedbackParentModel.getFeedbackFourOptionsModel().getOptions().get(3);

            }
        });


        verygood_cd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick_2.setVisibility(View.VISIBLE);
                good_tick_2.setVisibility(View.GONE);

                tempAnswer = feedbackParentModel.getFeedbackTwoOptionsModel().getOptions().get(0);

            }
        });
        good_cd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick_2.setVisibility(View.GONE);
                good_tick_2.setVisibility(View.VISIBLE);

                tempAnswer = feedbackParentModel.getFeedbackTwoOptionsModel().getOptions().get(1);

            }
        });



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition+1< feedbackParentModels.size()){
                    if (!TextUtils.isEmpty(tempAnswer)){
                        answers.add(tempAnswer);
                        tempAnswer = "";
                        currentPosition=currentPosition+1;
                        feedbackParentModel = feedbackParentModels.get(currentPosition);
                        setView(feedbackParentModel);
                    }else{
                        Utility.showToast(getActivity(), "Please answer before moving ahead");
                    }
                }else if (currentPosition + 1 == feedbackParentModels.size()){
                    tempAnswer = editText.getText().toString();
                    if (!TextUtils.isEmpty(tempAnswer)){
                        answers.add(tempAnswer);
                        Log.d("feedback_debug", answers+"");
                        mViewModel.saveFeedback(prefManager.getString(Constants.JWTTOKEN), answers)
                                .observe(getActivity(), new Observer<FeedbacksubmitModel>() {
                                    @Override
                                    public void onChanged(FeedbacksubmitModel feedbacksubmitModel) {
                                        if (feedbacksubmitModel!=null){
                                            if (feedbacksubmitModel.getError_code() == 0){
                                                Utility.showToast(getActivity(), "Thank you for your time!");
                                                tempAnswer="";
                                                getActivity().onBackPressed();
                                            }else{
                                                Utility.showToast(getActivity(), "Unable to save feedback at the moment");
                                                tempAnswer="";
                                            }
                                        }
                                    }
                                });
                    }else{
                        Utility.showToast(getActivity(), "Please answer before moving ahead");
                    }
                }
            }
        });


    }

    private void setView(FeedbackParentModel feedbackParentModel) {
        if (feedbackParentModel.getViewType() == 1){
            linUserInput.setVisibility(View.GONE);
            linTwoOptions.setVisibility(View.GONE);
            linFourOptions.setVisibility(View.VISIBLE);
            setDataOnFourOptionsView(feedbackParentModel.getFeedbackFourOptionsModel());

        }else if (feedbackParentModel.getViewType() == 2){
            linUserInput.setVisibility(View.GONE);
            linFourOptions.setVisibility(View.GONE);
            linTwoOptions.setVisibility(View.VISIBLE);
            setDataOnTwoOptionsView(feedbackParentModel.getFeedbackTwoOptionsModel());

        }else if (feedbackParentModel.getViewType() == 3){
            linFourOptions.setVisibility(View.GONE);
            linTwoOptions.setVisibility(View.GONE);
            linUserInput.setVisibility(View.VISIBLE);
            nextButton.setText("Submit");
            setDataonUserInputView(feedbackParentModel.getFeedbackInoutModel());

        }
    }

//    @Override
//    public void onNextClicked(FeedbackFourOptionsModel feedbackFourOptionsModel, int size) {
//        if (viewPager.getCurrentItem() == 1){
//            feedbackFourOptionsModelList.clear();
//        }
//        if (viewPager.getCurrentItem() +1 < size){
//            viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
//            feedbackFourOptionsModelList.add(feedbackFourOptionsModel);
//        }else{
//            Utility.showToast(getActivity(), "Thank you for your time!");
//            feedbackFourOptionsModelList.add(feedbackFourOptionsModel);
//            Log.d("feedback_debug", feedbackFourOptionsModelList +"");
//        }
//    }

    private void setDataonUserInputView(FeedbackInoutModel feedbackInoutModel) {
        tvQuestion_3.setText(feedbackInoutModel.getQuestion());
        editText.setHint("Enter your feedback...");
    }

    private void setDataOnTwoOptionsView(FeedbackTwoOptionsModel feedbackTwoOptionsModel) {
        tvQuestion_2.setText(feedbackTwoOptionsModel.getQuestion());
        tvOption1_2.setText(feedbackTwoOptionsModel.getOptions().get(0));
        tvOption2_2.setText(feedbackTwoOptionsModel.getOptions().get(1));

        verygood_tick_2.setVisibility(View.GONE);
        good_tick_2.setVisibility(View.GONE);


    }

    private void setDataOnFourOptionsView(FeedbackFourOptionsModel feedbackFourOptionsModel) {
        tvQuestion.setText(feedbackFourOptionsModel.getQuestion());
        tvOption1.setText(feedbackFourOptionsModel.getOptions().get(0));
        tvOption2.setText(feedbackFourOptionsModel.getOptions().get(1));
        tvOption3.setText(feedbackFourOptionsModel.getOptions().get(2));
        tvOption4.setText(feedbackFourOptionsModel.getOptions().get(3));

        verygood_tick.setVisibility(View.GONE);
        good_tick.setVisibility(View.GONE);
        fair_tick.setVisibility(View.GONE);
        bad_tick.setVisibility(View.GONE);


    }

    private void bindUserInputOptionsLayout(View itemView) {
        linUserInput = itemView.findViewById(R.id.feedback_userinput_lin);
        tvQuestion_3 = itemView.findViewById(R.id.tv_feedback_question_3);
        editText = itemView.findViewById(R.id.user_feedback_input_et);


    }

    private void binTwoOptionsLayout(View itemView) {
        linTwoOptions = itemView.findViewById(R.id.feedback_two_options_lin);

        verygood_cd_2=itemView.findViewById(R.id.cd1_2);
        good_cd_2=itemView.findViewById(R.id.cd2_2);
        verygood_tick_2=itemView.findViewById(R.id.imagetick1_2);
        good_tick_2=itemView.findViewById(R.id.imagetick2_2);
        tvOption1_2 = itemView.findViewById(R.id.tv_yes);
        tvOption2_2 = itemView.findViewById(R.id.tv_no);
        tvQuestion_2 = itemView.findViewById(R.id.tv_feedback_question_2);

        verygood_tick_2.setVisibility(View.GONE);
        good_tick_2.setVisibility(View.GONE);


    }

    private void bindFourOptionsLayout(View itemView) {

        linFourOptions = itemView.findViewById(R.id.feedback_four_options_lin);


        verygood_cd=itemView.findViewById(R.id.cd1);
        good_cd=itemView.findViewById(R.id.cd2);
        fair_cd=itemView.findViewById(R.id.cd3);
        bad_cd=itemView.findViewById(R.id.cd4);
        verygood_tick=itemView.findViewById(R.id.imagetick1);
        good_tick=itemView.findViewById(R.id.imagetick2);
        fair_tick=itemView.findViewById(R.id.imagetick3);
        bad_tick=itemView.findViewById(R.id.imagetick4);
        imageView1 = itemView.findViewById(R.id.imageview1);
        imageView2 = itemView.findViewById(R.id.imageview2);
        imageView3 = itemView.findViewById(R.id.imageview3);
        imageView4 = itemView.findViewById(R.id.imageview4);
        tvOption1 = itemView.findViewById(R.id.tv_verygood);
        tvOption2 = itemView.findViewById(R.id.tv_good);
        tvOption3 = itemView.findViewById(R.id.tv_fair);
        tvOption4 = itemView.findViewById(R.id.tv_bad);
        tvQuestion = itemView.findViewById(R.id.tv_feedback_question);

        verygood_tick.setVisibility(View.GONE);
        good_tick.setVisibility(View.GONE);
        fair_tick.setVisibility(View.GONE);
        bad_tick.setVisibility(View.GONE);

        //verygood_cd.setOnClickListener(this);

        verygood_tick.setVisibility(View.GONE);
        good_tick.setVisibility(View.GONE);
        fair_tick.setVisibility(View.GONE);
        bad_tick.setVisibility(View.GONE);



        Glide.with(getActivity())
                .load(R.drawable.wowsmiley)
                .centerCrop()
                .into(imageView1);
        Glide.with(getActivity())
                .load(R.drawable.goodsmley)
                .centerCrop()
                .into(imageView2);
        Glide.with(getActivity())
                .load(R.drawable.fairsmley)
                .centerCrop()
                .into(imageView3);
        Glide.with(getActivity())
                .load(R.drawable.sadsmley)
                .centerCrop()
                .into(imageView4);


    }
}
