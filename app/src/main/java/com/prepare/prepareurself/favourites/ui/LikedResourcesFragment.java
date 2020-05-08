package com.prepare.prepareurself.favourites.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class LikedResourcesFragment extends Fragment implements LikedResourcesAdapter.LikedResourceRvInteractor {

    private RecyclerView recyclerView;
    private FavouritesViewModel viewModel;
    private PrefManager prefManager;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
        prefManager = new PrefManager(getActivity());

        viewModel.fetchFavourites(prefManager.getString(Constants.JWTTOKEN),"resource",10,1);

        final LikedResourcesAdapter adapter = new LikedResourcesAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.getLikedResourcesModelLiveData().observe(getActivity(), new Observer<List<LikedResourcesModel>>() {
            @Override
            public void onChanged(List<LikedResourcesModel> likedResourcesModels) {
                if (likedResourcesModels!=null){
                    Log.d("liked_project", "onChanged: "+likedResourcesModels);
                    adapter.setLikedResourcesModels(likedResourcesModels);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onResourceClicked(LikedResourcesModel resource) {

    }

    @Override
    public void OnLikeButtonClicked(LikedResourcesModel resource, int checked) {

    }

    @Override
    public void onResourceShared(Bitmap bitmap, String text) {

    }
}
