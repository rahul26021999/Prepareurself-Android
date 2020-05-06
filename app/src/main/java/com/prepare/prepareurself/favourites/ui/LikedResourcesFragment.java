package com.prepare.prepareurself.favourites.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepare.prepareurself.R;

public class LikedResourcesFragment extends Fragment {

    private RecyclerView recyclerView;

    public LikedResourcesFragment() {
        // Required empty public constructor
    }

    public static LikedResourcesFragment newInstance() {
        LikedResourcesFragment fragment = new LikedResourcesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_liked_resources, container, false);

        recyclerView = view.findViewById(R.id.like_resources_rv);

        return view;
    }
}
