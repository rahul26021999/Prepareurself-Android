package com.prepare.prepareurself.favourites.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
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
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;

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

        viewModel.getFavourites(prefManager.getString(Constants.JWTTOKEN),"project",10,rvCurrentPage);
        rvCurrentPage+=1;

        final LikedProjectsRvAdapter adapter = new LikedProjectsRvAdapter(getActivity(), this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.fetchFavourites()
                .observe(getActivity(), new Observer<FavouritesResponseModel>() {
                    @Override
                    public void onChanged(final FavouritesResponseModel favouritesResponseModel) {
                        if (favouritesResponseModel!=null && favouritesResponseModel.getProjects()!=null){
                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    isScrolling = true;
                                }

                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);

                                    rvCurrentItems = layoutManager.getChildCount();
                                    rvTotalItems = layoutManager.getItemCount();
                                    rvScrolledOutItems = layoutManager.findFirstVisibleItemPosition();

                                    rvLastPage = favouritesResponseModel.getProjects().getLast_page();

                                    Log.d("paging_debug",rvCurrentPage+","+rvLastPage);

                                    if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage<=rvLastPage){
                                        isScrolling = false;
                                        viewModel.getFavourites(prefManager.getString(Constants.JWTTOKEN),"project",10,rvCurrentPage);
                                        rvCurrentPage+=1;
                                    }

                                }
                            });
                        }
                    }
                });

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
