package com.prepare.prepareurself.Home.content.resources.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
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
import android.widget.Toast;

import com.prepare.prepareurself.Home.content.dashboard.ui.fragment.DashboardFragment;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.Home.content.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.TheoryResourcesRvAdapter;
import com.prepare.prepareurself.Home.content.resources.viewmodel.ResourceViewModel;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class TheoryResourceFragment extends Fragment implements TheoryResourcesRvAdapter.TheoryResourceRvInteractor {

    private ResourceViewModel mViewModel;
    private RecyclerView rvTheoryResources;
    private TheoryResourcesRvAdapter adapter1;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;

    private TheoryResourceFragmentInteractor listener;

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
               adapter1.setResourcesList(resourceModels);
               adapter1.notifyDataSetChanged();
           }
       });
    }

    @Override
    public void onResourceClicked(ResourceModel resource) {
        Utility.redirectUsingCustomTab(getActivity(),resource.getLink());
        mViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN),resource.getId());
    }

    @Override
    public void OnLikeButtonClicked(ResourceModel resource, Boolean checked) {
        Toast.makeText(getContext(),"Liked button clicked",Toast.LENGTH_SHORT).show();
        //liikkkee code added
        mViewModel.resourcesLiked(prefManager.getString(Constants.JWTTOKEN),resource.getId(),checked);
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
