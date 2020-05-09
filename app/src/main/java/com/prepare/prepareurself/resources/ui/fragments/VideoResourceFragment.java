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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

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
    public void videoClicked(final ResourceModel videoResources, String videoCode, Bitmap bitmap) {

        if (videoResources.getLink().contains("youtu.be") || videoResources.getLink().contains("youtube")){
            if (videoResources.getView() == 0){
                if (getActivity()!=null){
                    mViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN),videoResources.getId())
                            .observe(getActivity(), new Observer<ResourceViewsResponse>() {
                                @Override
                                public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                    if (resourceViewsResponse!=null){
                                        if (resourceViewsResponse.getError_code() == 0){
                                            videoResources.setView(1);
                                            mViewModel.saveResource(videoResources);
                                        }
                                    }
                                }
                            });
                }
            }

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
        }


    }

    @Override
    public void onVideoResourceLiked(ResourceModel resourceModel, int liked) {
        mViewModel.resourcesLiked(prefManager.getString(Constants.JWTTOKEN), resourceModel.getId(), liked);
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
