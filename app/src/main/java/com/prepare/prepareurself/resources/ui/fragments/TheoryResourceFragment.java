package com.prepare.prepareurself.resources.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.resources.ui.adapter.TheoryResourcesRvAdapter;
import com.prepare.prepareurself.resources.viewmodel.ResourceViewModel;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class TheoryResourceFragment extends Fragment implements TheoryResourcesRvAdapter.TheoryResourceRvInteractor {

    private ResourceViewModel mViewModel;
    private RecyclerView rvTheoryResources;
    private TheoryResourcesRvAdapter adapter1;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;

    private TheoryResourceFragmentInteractor listener;
    private TextView tvCmingSoon;

    public interface TheoryResourceFragmentInteractor{
        void shareContent(Bitmap bitmap, String text);
    }

    public static TheoryResourceFragment newInstance() {
        return new TheoryResourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
    View view1=inflater.inflate(R.layout.theory_resource_fragment,container,false);
    rvTheoryResources=view1.findViewById(R.id.rv_theory_resources);
    tvCmingSoon = view1.findViewById(R.id.tv_coming_soon);
        return view1;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        prefManager = new PrefManager(getActivity());

        adapter1=new TheoryResourcesRvAdapter(getActivity(), this);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvTheoryResources.setLayoutManager(layoutManager);
        rvTheoryResources.setAdapter(adapter1);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(),R.drawable.theory_resource_divider);
        rvTheoryResources.addItemDecoration(decoration);

        if (ResourcesActivity.topicID!=-1){
            mViewModel.fetchResources(prefManager.getString(Constants.JWTTOKEN),
                    ResourcesActivity.topicID,
                    rvCurrentPage,
                    10,
                    Constants.THEORY);
            rvCurrentPage+=1;
        }

        mViewModel.getResponseLiveData().observe(getActivity(), new Observer<ResourcesResponse>() {
            @Override
            public void onChanged(final ResourcesResponse resourcesResponse) {
                if (resourcesResponse !=null){
                    rvTheoryResources.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                        Constants.THEORY);
                                rvCurrentPage+=1;
                            }

                        }
                    });
                }
            }
        });

       mViewModel.getListLiveData(ResourcesActivity.topicID,Constants.THEORY).observe(getActivity(), new Observer<List<ResourceModel>>() {
           @Override
           public void onChanged(List<ResourceModel> resourceModels) {
              if (resourceModels!=null && !resourceModels.isEmpty()){
                  tvCmingSoon.setVisibility(View.GONE);
                  rvTheoryResources.setVisibility(View.VISIBLE);
                  adapter1.setResourcesList(resourceModels);
                  adapter1.notifyDataSetChanged();
              }else{
                  tvCmingSoon.setText("Coming Soon...");
                  tvCmingSoon.setVisibility(View.VISIBLE);
                  rvTheoryResources.setVisibility(View.GONE);
              }
           }
       });
    }

    @Override
    public void onResourceClicked(final ResourceModel resource) {
        Utility.redirectUsingCustomTab(getActivity(),resource.getLink());
        if (resource.getView()==0 && getActivity()!=null){
            mViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN),resource.getId())
                    .observe(getActivity(), new Observer<ResourceViewsResponse>() {
                        @Override
                        public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                            if (resourceViewsResponse!=null && resourceViewsResponse.getError_code() == 0){
                                resource.setView(1);
                                mViewModel.saveResource(resource);
                                Log.d("resource_viewed",resourceViewsResponse.getMessage());
                            }
                        }
                    });
        }

    }

    @Override
    public void OnLikeButtonClicked(final ResourceModel resource, final int checked) {
        mViewModel.resourcesLiked(prefManager.getString(Constants.JWTTOKEN), resource.getId(), checked)
                .observe(getActivity(), new Observer<ResourceLikesResponse>() {
                    @Override
                    public void onChanged(ResourceLikesResponse resourceLikesResponse) {
                        if (resourceLikesResponse!=null){
                            if (!resourceLikesResponse.getSuccess()){
                                Utility.showToast(getActivity(),"Unable to like at the moment");
                            }else{
                                if (checked == 0){
                                    resource.setLike(1);
                                    resource.setTotal_likes(resource.getTotal_likes()+1);
                                }else if (checked == 1){
                                    resource.setLike(0);
                                    resource.setTotal_likes(resource.getTotal_likes()-1);
                                }
                                mViewModel.saveResource(resource);
                                adapter1.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onResourceShared(Bitmap bitmap, String text) {
       // Utility.shareContent(getActivity(),bitmap,text);
        listener.shareContent(bitmap, text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TheoryResourceFragment.TheoryResourceFragmentInteractor) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TheoryResourceFragmentInteractor");
        }

    }
}
