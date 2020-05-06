package com.prepare.prepareurself.favourites.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepare.prepareurself.R;

public class LikedProjectFragment extends Fragment {

    private RecyclerView recyclerView;

    public LikedProjectFragment() {
        // Required empty public constructor
    }

    public static LikedProjectFragment newInstance() {
        LikedProjectFragment fragment = new LikedProjectFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_liked_project, container, false);
        recyclerView = view.findViewById(R.id.like_projects_rv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
}
