package com.prepare.prepareurself.feedback.ui;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.dashboard.ui.adapters.CustomPagerAdapter;
import com.prepare.prepareurself.feedback.data.model.FeedbackModel;
import com.prepare.prepareurself.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFragment extends Fragment implements CustomPagerAdapter.FeedbackPagerListener {

    Button next_btn;
    private FeedbackViewModel mViewModel;
    private ViewPager viewPager;
    private CustomPagerAdapter customPagerAdapter;
    private List<FeedbackModel> feedbackModelList;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.feedback_fragment, container, false);
        next_btn=v.findViewById(R.id.btn_next);
        viewPager = v.findViewById(R.id.feedback_pager);

        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);

        final List<FeedbackModel> feedbackModels = mViewModel.getFeebacks();

        feedbackModelList = new ArrayList<>();

        customPagerAdapter = new CustomPagerAdapter(getActivity(),feedbackModels, this);
        viewPager.setAdapter(customPagerAdapter);


    }

    @Override
    public void onNextClicked(FeedbackModel feedbackModel, int size) {
        if (viewPager.getCurrentItem() == 1){
            feedbackModelList.clear();
        }
        if (viewPager.getCurrentItem() +1 < size){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
            feedbackModelList.add(feedbackModel);
        }else{
            Utility.showToast(getActivity(), "Thank you for your time!");
            feedbackModelList.add(feedbackModel);
            Log.d("feedback_debug",feedbackModelList+"");
        }
    }
}
