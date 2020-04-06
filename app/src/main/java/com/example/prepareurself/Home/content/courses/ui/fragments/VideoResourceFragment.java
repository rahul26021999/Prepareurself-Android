package com.example.prepareurself.Home.content.courses.ui.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prepareurself.Home.content.courses.viewmodel.VideoResourceViewModel;
import com.example.prepareurself.R;

public class VideoResourceFragment extends Fragment {

    private VideoResourceViewModel mViewModel;

    public static VideoResourceFragment newInstance() {
        return new VideoResourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_resource_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VideoResourceViewModel.class);
        // TODO: Use the ViewModel
    }

}
