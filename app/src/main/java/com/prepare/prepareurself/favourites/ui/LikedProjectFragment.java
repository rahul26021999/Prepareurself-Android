package com.prepare.prepareurself.favourites.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.RelativeLayout;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.io.IOException;
import java.util.List;

public class LikedProjectFragment extends Fragment implements LikedProjectsRvAdapter.LikedProjectsRvInteractor {

    private RecyclerView recyclerView;
    private FavouritesViewModel viewModel;
    private PrefManager prefManager;
    private RelativeLayout emptyLayout;

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
        emptyLayout = view.findViewById(R.id.emptyFavourites);


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
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.getLikedProjectsModelLiveData().observe(getActivity(), new Observer<List<LikedProjectsModel>>() {
            @Override
            public void onChanged(List<LikedProjectsModel> likedProjectsModel) {
                if (likedProjectsModel!=null && !likedProjectsModel.isEmpty()){
                    adapter.setLikedProjectsModels(likedProjectsModel);
                    adapter.notifyDataSetChanged();
                    emptyLayout.setVisibility(View.GONE);
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onProjectClicked(LikedProjectsModel projectsModel) {
        Intent intent = new Intent(getActivity(), ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onProjectShared(Bitmap bitmap, String text) {
        try {
            Uri bitmapUri = Utility.getUriOfBitmap(bitmap, getActivity());
            Utility.shareContent(getActivity(),bitmapUri,text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProjectLiked(LikedProjectsModel projectsModel, int liked) {
        viewModel.likeProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId(), liked);
    }
}
