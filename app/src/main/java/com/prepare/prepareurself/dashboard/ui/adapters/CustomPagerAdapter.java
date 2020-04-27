package com.prepare.prepareurself.dashboard.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.feedback.data.model.FeedbackModel;

import java.util.ArrayList;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private List<FeedbackModel> feedbackModels;
    private Context context;
    private LayoutInflater layoutInflater;
    private int customPosition = 0;
    private FeedbackPagerListener listener;

    public CustomPagerAdapter(Context context, List<FeedbackModel> feedbackModels, FeedbackPagerListener listener) {
        this.context = context;
        this.feedbackModels = feedbackModels;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        if (feedbackModels!=null){
            return feedbackModels.size();
        }else{
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView =  layoutInflater.inflate(R.layout.swipe_layout,container,false);
        TextView textView = itemView.findViewById(R.id.tv_feedback_question);
        final ImageView verygood_tick, good_tick, fair_tick, bad_tick,imageView1,imageView2,imageView3,imageView4;
        CardView verygood_cd , good_cd, fair_cd, bad_cd;
        Button nextButton = itemView.findViewById(R.id.btn_next);

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

        final FeedbackModel feedbackModel = feedbackModels.get(position);

        textView.setText(feedbackModel.getQuestion());

        final FeedbackModel model = new FeedbackModel();
        model.setId(feedbackModel.getId());
        model.setQuestion(feedbackModel.getQuestion());

        verygood_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.VISIBLE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
                model.setResponse(1);
            }
        });
        good_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.VISIBLE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
                model.setResponse(2);
            }
        });
        fair_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.VISIBLE);
                bad_tick.setVisibility(View.GONE);
                model.setResponse(3);
            }
        });
        bad_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.VISIBLE);
                model.setResponse(4);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNextClicked(model, feedbackModels.size());
            }
        });


        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    public interface FeedbackPagerListener{
        void onNextClicked(FeedbackModel feedbackModel, int size);
    }

}
