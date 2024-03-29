package com.prepare.prepareurself.resources.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.resources.ui.adapter.VideoResoursesRvAdapter;
import com.prepare.prepareurself.resources.viewmodel.ResourceViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import java.io.IOException;
import java.util.List;

public class VideoResourceFragment extends Fragment implements VideoResoursesRvAdapter.VideoResourceInteractor {

    private ResourceViewModel mViewModel;
    private RecyclerView rvVideoResources;
    private VideoResoursesRvAdapter adapter;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private TextView tvComingSoon;

    public static VideoResourceFragment newInstance() {
        return new VideoResourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_resource_fragment, container, false);

        rvVideoResources = view.findViewById(R.id.rv_video_resources);
        tvComingSoon = view.findViewById(R.id.tv_coming_soon);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        prefManager = new PrefManager(getActivity());

        adapter = new VideoResoursesRvAdapter(getActivity(), this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvVideoResources.setLayoutManager(layoutManager);
      //  rvVideoResources.addItemDecoration(new DividerItemDecoration(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(),R.drawable.theory_resource_divider);
        rvVideoResources.addItemDecoration(decoration);
        rvVideoResources.setAdapter(adapter);

        if (ResourcesActivity.topicID!=-1){
            mViewModel.fetchResources(prefManager.getString(Constants.JWTTOKEN),
                    ResourcesActivity.topicID,
                    rvCurrentPage,
                    10,
                    Constants.VIDEO);
            rvCurrentPage+=1;
        }

        mViewModel.getResponseLiveData().observe(getActivity(), new Observer<ResourcesResponse>() {
            @Override
            public void onChanged(final ResourcesResponse resourcesResponse) {
                if (resourcesResponse !=null){
                    rvVideoResources.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            rvCurrentItems = layoutManager.getChildCount();
                            rvTotalItems = layoutManager.getItemCount();
                            rvScrolledOutItems = layoutManager.findFirstVisibleItemPosition();

                            rvLastPage = resourcesResponse.getLast_page();

                            if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage <= rvLastPage){
                                isScrolling = false;
                                mViewModel.fetchResources(prefManager.getString(Constants.JWTTOKEN),
                                        ResourcesActivity.topicID,
                                        rvCurrentPage,
                                        10,
                                        Constants.VIDEO);
                                rvCurrentPage+=1;

                            }

                        }
                    });
                }
            }
        });

        mViewModel.getListLiveData(ResourcesActivity.topicID,Constants.VIDEO).observe(getActivity(), new Observer<List<ResourceModel>>() {
            @Override
            public void onChanged(final List<ResourceModel> resourceModels) {

                if (resourceModels!=null && !resourceModels.isEmpty()){
                    tvComingSoon.setVisibility(View.GONE);
                    rvVideoResources.setVisibility(View.VISIBLE);
                    adapter.setResourceModels(resourceModels);
                    adapter.notifyDataSetChanged();
                }else{
                    tvComingSoon.setText("Coming Soon...");
                    tvComingSoon.setVisibility(View.VISIBLE);
                    rvVideoResources.setVisibility(View.GONE);
                }


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void videoClicked(final ResourceModel videoResources, final String videoCode, final Bitmap bitmap, final int view, final int total_views) {

        if (videoResources.getLink().contains("youtu.be") || videoResources.getLink().contains("youtube")){

            Intent intent = new Intent(getActivity(), VideoActivity.class);
            intent.putExtra(Constants.VIDEOCODE,videoCode);
            intent.putExtra(Constants.RESOURCEID,videoResources.getId());
            intent.putExtra(Constants.RESOURCEVIDEO,true);
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
            if (videoResources!=null){
                Log.d("resource_viewed","beforeliked : "+videoResources.getView()+", "+videoResources.getTotal_views()+", "+videoResources.getId());
                if (videoResources.getView() == 0){
                    mViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN), videoResources.getId())
                            .observeForever(new Observer<ResourceViewsResponse>() {
                                @Override
                                public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                    if (resourceViewsResponse.getError_code() == 0){
                                        videoResources.setView(1);
                                        Log.d("resource_viewed","onliked begore: "+videoResources.getView()+", "+videoResources.getTotal_views()+", "+videoResources.getId());
                                        videoResources.setTotal_views(videoResources.getTotal_views()+1);
                                        Log.d("resource_viewed","onliked : "+videoResources.getView()+", "+videoResources.getTotal_views()+", "+videoResources.getId());
                                        mViewModel.saveResource(videoResources);
                                    }
                                }
                            });
                }
            }
        }


    }

    @Override
    public void onVideoResourceLiked(final ResourceModel resourceModel, final int liked) {
        mViewModel.resourcesLiked(prefManager.getString(Constants.JWTTOKEN), resourceModel.getId(), liked)
                .observe(getActivity(), new Observer<ResourceLikesResponse>() {
                    @Override
                    public void onChanged(ResourceLikesResponse resourceLikesResponse) {
                        if (resourceLikesResponse!=null){
                            if (!resourceLikesResponse.getSuccess()){
                                Utility.showToast(getActivity(),"Unable to like at the moment");
                            }else{
                                if (liked == 0){
                                    resourceModel.setLike(1);
                                    resourceModel.setTotal_likes(resourceModel.getTotal_likes()+1);
                                }else if (liked == 1){
                                    resourceModel.setLike(0);
                                    resourceModel.setTotal_likes(resourceModel.getTotal_likes()-1);
                                }
                                mViewModel.saveResource(resourceModel);
                                adapter.notifyDataSetChanged();

                                LikedResourcesModel likedResourcesModel = new LikedResourcesModel();
                                likedResourcesModel.setId(resourceModel.getId());
                                likedResourcesModel.setTitle(resourceModel.getTitle());
                                likedResourcesModel.setDescription(resourceModel.getDescription());
                                likedResourcesModel.setType(resourceModel.getType());
                                likedResourcesModel.setImage_url(resourceModel.getImage_url());
                                likedResourcesModel.setLink(resourceModel.getLink());
                                likedResourcesModel.setCourse_topic_id(resourceModel.getCourse_topic_id());
                                likedResourcesModel.setAdmin_id(resourceModel.getAdmin_id());
                                likedResourcesModel.setCreated_at(resourceModel.getCreated_at());
                                likedResourcesModel.setUpdated_at(resourceModel.getUpdated_at());
                                likedResourcesModel.setLike(resourceModel.getLike());
                                likedResourcesModel.setTotal_likes(resourceModel.getTotal_likes());
                                likedResourcesModel.setView(resourceModel.getView());
                                likedResourcesModel.setTotal_views(resourceModel.getTotal_views());

                                if (liked == 0){
                                    mViewModel.insertLikedResource(likedResourcesModel);
                                }else if (liked == 1){
                                    mViewModel.deleteLikedResource(likedResourcesModel);
                                }

                            }
                        }
                    }
                });
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
