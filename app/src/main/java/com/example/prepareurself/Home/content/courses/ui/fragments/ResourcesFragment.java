package com.example.prepareurself.Home.content.courses.ui.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prepareurself.Home.content.courses.viewmodels.ResourcesViewModel;
import com.example.prepareurself.R;

public class ResourcesFragment extends Fragment {

    private ResourcesViewModel mViewModel;

    public static ResourcesFragment newInstance() {
        return new ResourcesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resources_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ResourcesViewModel.class);
        // TODO: Use the ViewModel
    }

}
