package com.example.prepareurself.Home.content.courses.ui.fragments;

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

import com.example.prepareurself.Home.content.courses.data.model.Resource;
import com.example.prepareurself.Home.content.courses.data.model.TopicsModel;
import com.example.prepareurself.Home.content.courses.ui.activity.CoursesActivity;
import com.example.prepareurself.Home.content.courses.ui.adapters.ResourcesRvAdapter;
import com.example.prepareurself.Home.content.courses.viewmodels.TopicViewModel;
import com.example.prepareurself.Home.content.resources.ui.activity.ResourcesActivity;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;

import java.util.List;

public class ResourcesFragment extends Fragment implements ResourcesRvAdapter.ResourceRvInteractor {

    private TopicViewModel mViewModel;
    private RecyclerView recyclerView;
    private ResourcesRvAdapter adapter;

    public static ResourcesFragment newInstance() {
        return new ResourcesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resources_fragment, container, false);
        recyclerView = view.findViewById(R.id.rv_resources);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        adapter = new ResourcesRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

       mViewModel.getLiveData(CoursesActivity.courseId).observe(getActivity(), new Observer<List<TopicsModel>>() {
           @Override
           public void onChanged(List<TopicsModel> topicsModels) {
               adapter.setTopics(topicsModels);
               adapter.notifyDataSetChanged();
           }
       });

    }

    @Override
    public void onResourceClicked(TopicsModel topicsModel) {
        Intent intent = new Intent(getActivity(), ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        startActivity(intent);
    }
}
