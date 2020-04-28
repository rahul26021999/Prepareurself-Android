package com.prepare.prepareurself.dashboard.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.feedback.data.model.FeedbackFourOptionsModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackInoutModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackParentModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackTwoOptionsModel;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private List<FeedbackParentModel> feedbackParentModels;
    private Context context;
    private LayoutInflater layoutInflater;
    private int customPosition = 0;
    private FeedbackPagerListener listener;
    private LinearLayout linUserInput, linTwoOptions, linFourOptions;
    private ImageView verygood_tick_2, good_tick_2;
    private CardView verygood_cd_2 , good_cd_2;
    private TextView tvOption1_2, tvOption2_2, tvQuestion_2;

    private ImageView verygood_tick, good_tick, fair_tick, bad_tick,imageView1,imageView2,imageView3,imageView4;
    private CardView verygood_cd , good_cd, fair_cd, bad_cd;
    private TextView tvOption1, tvOption2, tvOption3, tvOption4, tvQuestion;

    private TextView tvQuestion_3;
    private EditText editText;

    public CustomPagerAdapter(Context context, List<FeedbackParentModel> feedbackParentModels, FeedbackPagerListener listener) {
        this.context = context;
        this.feedbackParentModels = feedbackParentModels;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        if (feedbackParentModels !=null){
            return feedbackParentModels.size();
        }else{
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =  layoutInflater.inflate(R.layout.swipe_layout,container,false);
        Button nextButton = itemView.findViewById(R.id.btn_next);


        bindFourOptionsLayout(itemView);
        binTwoOptionsLayout(itemView);
        bindUserInputOptionsLayout(itemView);

        FeedbackParentModel feedbackParentModel = feedbackParentModels.get(position);

//        verygood_cd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                verygood_tick.setVisibility(View.VISIBLE);
//                good_tick.setVisibility(View.GONE);
//                fair_tick.setVisibility(View.GONE);
//                bad_tick.setVisibility(View.GONE);
//                verygood_cd.setBackgroundColor(Color.GREEN);
//                Utility.showToast(context, "clicked");
//            }
//        });
        good_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.VISIBLE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
                Utility.showToast(context, "clicked");
            }
        });
        fair_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.VISIBLE);
                bad_tick.setVisibility(View.GONE);
                Utility.showToast(context, "clicked");
            }
        });
        bad_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.VISIBLE);
                Utility.showToast(context, "clicked");
            }
        });


        verygood_cd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick_2.setVisibility(View.VISIBLE);
                good_tick_2.setVisibility(View.GONE);
                Utility.showToast(context, "clicked");
            }
        });
        good_cd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick_2.setVisibility(View.GONE);
                good_tick_2.setVisibility(View.VISIBLE);
                Utility.showToast(context, "clicked");
            }
        });

        if (feedbackParentModel.getViewType() == 1){
            linUserInput.setVisibility(View.GONE);
            linTwoOptions.setVisibility(View.GONE);
            linFourOptions.setVisibility(View.VISIBLE);
          //  setDataOnFourOptionsView(feedbackParentModel.getFeedbackFourOptionsModel());

        }else if (feedbackParentModel.getViewType() == 2){
            linUserInput.setVisibility(View.GONE);
            linFourOptions.setVisibility(View.GONE);
            linTwoOptions.setVisibility(View.VISIBLE);
      //      setDataOnTwoOptionsView(feedbackParentModel.getFeedbackTwoOptionsModel());

        }else if (feedbackParentModel.getViewType() == 3){
            linFourOptions.setVisibility(View.GONE);
            linTwoOptions.setVisibility(View.GONE);
            linUserInput.setVisibility(View.VISIBLE);
        //    setDataonUserInputView(feedbackParentModel.getFeedbackInoutModel());

        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showToast(context, "clicked next");
            }
        });



        container.addView(itemView);
        return itemView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == verygood_cd.getId()){
            Log.d("visibility_debug",verygood_cd.getVisibility()+"");
            verygood_tick.setVisibility(View.VISIBLE);
            Log.d("visibility_debug",verygood_cd.getVisibility()+"");
            verygood_cd.setCardBackgroundColor(context.getResources().getColor(R.color.like_blue));
        }
    }

    private void setDataonUserInputView(FeedbackInoutModel feedbackInoutModel) {
        tvQuestion_3.setText(feedbackInoutModel.getQuestion());
        editText.setHint("Enter your feedback...");
    }

    private void setDataOnTwoOptionsView(FeedbackTwoOptionsModel feedbackTwoOptionsModel) {
        tvQuestion_2.setText(feedbackTwoOptionsModel.getQuestion());
        tvOption1_2.setText(feedbackTwoOptionsModel.getOptions().get(0));
        tvOption2_2.setText(feedbackTwoOptionsModel.getOptions().get(1));



    }

    private void setDataOnFourOptionsView(FeedbackFourOptionsModel feedbackFourOptionsModel) {
        tvQuestion.setText(feedbackFourOptionsModel.getQuestion());
        tvOption1.setText(feedbackFourOptionsModel.getOptions().get(0));
        tvOption2.setText(feedbackFourOptionsModel.getOptions().get(1));
        tvOption3.setText(feedbackFourOptionsModel.getOptions().get(2));
        tvOption4.setText(feedbackFourOptionsModel.getOptions().get(3));


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

        verygood_cd.setOnClickListener(this);



        Glide.with(context)
                .load(R.drawable.wowsmiley)
                .centerCrop()
                .into(imageView1);
        Glide.with(context)
                .load(R.drawable.goodsmley)
                .centerCrop()
                .into(imageView2);
        Glide.with(context)
                .load(R.drawable.fairsmley)
                .centerCrop()
                .into(imageView3);
        Glide.with(context)
                .load(R.drawable.sadsmley)
                .centerCrop()
                .into(imageView4);





    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    public interface FeedbackPagerListener{
        void onNextClicked(FeedbackFourOptionsModel feedbackFourOptionsModel, int size);
    }

}
