package com.prepare.prepareurself.favourites.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class LikedProjectFragment extends Fragment implements LikedProjectsRvAdapter.LikedProjectsRvInteractor {

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


        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        prefManager = new PrefManager(getActivity());

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.fetchFavourites(prefManager.getString(Constants.JWTTOKEN),"project",10,1);

        final LikedProjectsRvAdapter adapter = new LikedProjectsRvAdapter(getActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.getLikedProjectsModelLiveData().observe(getActivity(), new Observer<List<LikedProjectsModel>>() {
            @Override
            public void onChanged(List<LikedProjectsModel> likedProjectsModel) {
                if (likedProjectsModel!=null){
                    adapter.setLikedProjectsModels(likedProjectsModel);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onProjectClicked(LikedProjectsModel projectsModel) {

    }

    @Override
    public void onProjectShared(Bitmap bitmap, String text) {

    }

    @Override
    public void onProjectLiked(LikedProjectsModel projectsModel, int liked) {

    }
}
