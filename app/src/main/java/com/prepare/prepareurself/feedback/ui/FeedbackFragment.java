package com.prepare.prepareurself.feedback.ui;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;

public class FeedbackFragment extends Fragment {
    ImageView verygood_tick , good_tick, fair_tick, bad_tick,imageView1,imageView2,imageView3,imageView4;
    CardView verygood_cd , good_cd, fair_cd, bad_cd;
    Button next_btn;
    private FeedbackViewModel mViewModel;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.feedback_fragment, container, false);
        next_btn=v.findViewById(R.id.btn_next);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity()," II am workiing",Toast.LENGTH_SHORT).show();
            }
        });
        verygood_cd=v.findViewById(R.id.cd1);
        good_cd=v.findViewById(R.id.cd2);
        fair_cd=v.findViewById(R.id.cd3);
        bad_cd=v.findViewById(R.id.cd4);
        verygood_tick=v.findViewById(R.id.imagetick1);
        good_tick=v.findViewById(R.id.imagetick2);
        fair_tick=v.findViewById(R.id.imagetick3);
        bad_tick=v.findViewById(R.id.imagetick4);
        imageView1 = v.findViewById(R.id.imageview1);
        imageView2 = v.findViewById(R.id.imageview2);
        imageView3 = v.findViewById(R.id.imageview3);
        imageView4 = v.findViewById(R.id.imageview4);
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
        verygood_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.VISIBLE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
            }
        });
        good_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.VISIBLE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.GONE);
            }
        });
        fair_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.VISIBLE);
                bad_tick.setVisibility(View.GONE);
            }
        });
        bad_cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verygood_tick.setVisibility(View.GONE);
                good_tick.setVisibility(View.GONE);
                fair_tick.setVisibility(View.GONE);
                bad_tick.setVisibility(View.VISIBLE);
            }
        });
        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FeedbackViewModel.class);
        // TODO: Use the ViewModel
    }

}
