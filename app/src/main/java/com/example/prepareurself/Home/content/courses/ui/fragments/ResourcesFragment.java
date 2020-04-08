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

import com.example.prepareurself.Home.content.courses.model.Resource;
import com.example.prepareurself.Home.content.courses.ui.adapters.ResourcesRvAdapter;
import com.example.prepareurself.Home.content.courses.viewmodels.ResourcesViewModel;
import com.example.prepareurself.Home.content.resources.ui.activity.ResourcesActivity;
import com.example.prepareurself.R;

import java.util.List;

public class ResourcesFragment extends Fragment implements ResourcesRvAdapter.ResourceRvInteractor {

    private ResourcesViewModel mViewModel;
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
        mViewModel = ViewModelProviders.of(this).get(ResourcesViewModel.class);

        adapter = new ResourcesRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mViewModel.getListMutableLiveData().observe(getActivity(), new Observer<List<Resource>>() {
            @Override
            public void onChanged(List<Resource> resources) {
                adapter.setResources(resources);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onResourceClicked(Resource resource) {
        startActivity(new Intent(getActivity(), ResourcesActivity.class));
    }
}
