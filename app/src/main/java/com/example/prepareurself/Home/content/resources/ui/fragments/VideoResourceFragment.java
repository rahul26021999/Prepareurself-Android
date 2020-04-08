package com.example.prepareurself.Home.content.resources.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prepareurself.Home.content.resources.model.VideoResources;
import com.example.prepareurself.Home.content.resources.ui.adapter.VideoResoursesRvAdapter;
import com.example.prepareurself.Home.content.resources.viewmodel.VideoResourceViewModel;
import com.example.prepareurself.Home.content.resources.youtubevideoplayer.YoutubePlayerActivity;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;

import java.util.List;

public class VideoResourceFragment extends Fragment implements VideoResoursesRvAdapter.VideoResourceInteractor {

    private VideoResourceViewModel mViewModel;
    private RecyclerView rvVideoResources;
    private VideoResoursesRvAdapter adapter;

    public static VideoResourceFragment newInstance() {
        return new VideoResourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_resource_fragment, container, false);

        rvVideoResources = view.findViewById(R.id.rv_video_resources);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VideoResourceViewModel.class);

        adapter = new VideoResoursesRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvVideoResources.setLayoutManager(layoutManager);
        rvVideoResources.setAdapter(adapter);

        mViewModel.getListLiveData().observe(getActivity(), new Observer<List<VideoResources>>() {
            @Override
            public void onChanged(List<VideoResources> videoResources) {
                adapter.setVideoResources(videoResources);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void videoClicked(VideoResources videoResources) {
        Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
        intent.putExtra(Constants.VIDEOID,videoResources.getVideoCode());
        startActivity(intent);
    }
}
