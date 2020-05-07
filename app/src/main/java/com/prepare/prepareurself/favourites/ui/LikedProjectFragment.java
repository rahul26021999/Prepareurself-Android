package com.prepare.prepareurself.favourites.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

public class LikedProjectFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouritesViewModel viewModel;
    private PrefManager prefManager;

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
        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        prefManager = new PrefManager(getActivity());

        viewModel.fetchFavourites(prefManager.getString(Constants.JWTTOKEN),"project",10,1);

    }
}
