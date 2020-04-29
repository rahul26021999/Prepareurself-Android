package com.prepare.prepareurself.aboutus.ui;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Utility;

public class AboutusFragment extends Fragment implements View.OnClickListener {

    private AboutusViewModel mViewModel;
    private TextView title;
    private ImageView backBtn;
    private TextView tvTerms;

    private String TermsUrl = "http://prepareurself.in/terms-and-conditions";

    public static AboutusFragment newInstance() {
        return new AboutusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.aboutus_fragment, container, false);
        title = view.findViewById(R.id.title);
        backBtn = view.findViewById(R.id.backBtn);
        tvTerms = view.findViewById(R.id.tv_terms);

        backBtn.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        title.setText("About us");
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutusViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backBtn:
                getActivity().onBackPressed();
                break;

            case R.id.tv_terms:
                Utility.redirectUsingCustomTab(getActivity(),TermsUrl);
                break;
        }
    }
}
