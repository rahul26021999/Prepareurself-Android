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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.favourites.viewmodel.FavouritesViewModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import java.io.IOException;
import java.util.List;

public class LikedResourcesFragment extends Fragment implements LikedResourcesAdapter.LikedResourceRvInteractor {

    private RecyclerView recyclerView;
    private FavouritesViewModel viewModel;
    private PrefManager prefManager;
    private RelativeLayout emptyLayout;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private ProgressBar progressBar;

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
        emptyLayout = view.findViewById(R.id.emptyFavourites);
        progressBar = view.findViewById(R.id.like_resource_loader);

        progressBar.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
        prefManager = new PrefManager(getActivity());

        viewModel.getFavourites(prefManager.getString(Constants.JWTTOKEN),"resource",10,rvCurrentPage);
        rvCurrentPage+=1;

        progressBar.setVisibility(View.VISIBLE);

        final LikedResourcesAdapter adapter = new LikedResourcesAdapter(getActivity(), this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.fetchFavourites()
                .observe(getActivity(), new Observer<FavouritesResponseModel>() {
                    @Override
                    public void onChanged(final FavouritesResponseModel favouritesResponseModel) {
                        if (favouritesResponseModel!=null && favouritesResponseModel.getResources()!=null){
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

                                    rvLastPage = favouritesResponseModel.getResources().getLast_page();

                                    Log.d("paging_debug",rvCurrentPage+","+rvLastPage);

                                    if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage<=rvLastPage){
                                        isScrolling = false;
                                        viewModel.getFavourites(prefManager.getString(Constants.JWTTOKEN),"resource",10,rvCurrentPage);
                                        rvCurrentPage+=1;
                                    }

                                }
                            });
                        }
                    }
                });

        viewModel.getLikedResourcesModelLiveData().observe(getActivity(), new Observer<List<LikedResourcesModel>>() {
            @Override
            public void onChanged(List<LikedResourcesModel> likedResourcesModels) {

                if (likedResourcesModels!=null && !likedResourcesModels.isEmpty()){
                    Log.d("liked_project", "onChanged: "+likedResourcesModels);
                    adapter.setLikedResourcesModels(likedResourcesModels);
                    adapter.notifyDataSetChanged();
                    emptyLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void videoClicked(LikedResourcesModel videoResources, String videoCode, Bitmap bitmap) {
        if (videoResources.getLink().contains("youtu.be") || videoResources.getLink().contains("youtube")){

            Intent intent = new Intent(getActivity(), VideoActivity.class);
            intent.putExtra(Constants.VIDEOCODE,videoCode);
            intent.putExtra(Constants.RESOURCEID,videoResources.getId());
            intent.putExtra(Constants.RESOURCEVIDEOLIKED,true);
            intent.putExtra(Constants.VIDEOTITLE, videoResources.getTitle());
            intent.putExtra(Constants.VIDEODESCRIPTION, videoResources.getDescription());
            intent.putExtra(Constants.TOPICID, videoResources.getCourse_topic_id());

            try {
                Uri bitmapUri = Utility.getUriOfBitmap(bitmap, getActivity());
                intent.putExtra(Constants.BITMAPURI,bitmapUri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            startActivity(intent);

        }else{
            Utility.redirectUsingCustomTab(getActivity(),videoResources.getLink());
        }
    }

    @Override
    public void onResourceClicked(LikedResourcesModel resource) {
        Utility.redirectUsingCustomTab(getActivity(),resource.getLink());
    }

    @Override
    public void OnLikeButtonClicked(LikedResourcesModel resource, int checked) {
        viewModel.likeResource(prefManager.getString(Constants.JWTTOKEN),resource.getId(), checked);
    }

    @Override
    public void onResourceShared(Bitmap bitmap, String text) {
        try {
            Uri bitmapUri = Utility.getUriOfBitmap(bitmap, getActivity());
            Utility.shareContent(getActivity(),bitmapUri,text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
