package com.prepare.prepareurself.Home.content.resources.ui.fragments;

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

import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.Home.content.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.VideoResoursesRvAdapter;
import com.prepare.prepareurself.Home.content.resources.viewmodel.ResourceViewModel;
import com.prepare.prepareurself.Home.content.resources.youtubevideoplayer.YoutubePlayerActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;

import java.util.List;

public class VideoResourceFragment extends Fragment implements VideoResoursesRvAdapter.VideoResourceInteractor {

    private ResourceViewModel mViewModel;
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
        mViewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        adapter = new VideoResoursesRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvVideoResources.setLayoutManager(layoutManager);
        rvVideoResources.addItemDecoration(new DividerItemDecoration(getActivity()));
        rvVideoResources.setAdapter(adapter);

        mViewModel.getListLiveData(ResourcesActivity.topicID,Constants.VIDEO).observe(getActivity(), new Observer<List<ResourceModel>>() {
            @Override
            public void onChanged(List<ResourceModel> resourceModels) {
                adapter.setResourceModels(resourceModels);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void videoClicked(ResourceModel videoResources, String videoCode) {
        Intent intent = new Intent(getActivity(), YoutubePlayerActivity.class);
        intent.putExtra(Constants.VIDEOCODE,videoCode);
        intent.putExtra(Constants.VIDEOID,videoResources.getId());
        intent.putExtra(Constants.VIDEOTITLE, videoResources.getTitle());
        intent.putExtra(Constants.VIDEODESCRIPTION, videoResources.getDescription());

        startActivity(intent);
    }
}
