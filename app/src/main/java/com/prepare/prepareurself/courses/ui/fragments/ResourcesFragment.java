package com.prepare.prepareurself.courses.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.adapters.ResourcesRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.TopicViewModel;
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class ResourcesFragment extends Fragment implements ResourcesRvAdapter.ResourceRvInteractor {

    private TopicViewModel mViewModel;
    private RecyclerView recyclerView;
    private ResourcesRvAdapter adapter;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private PrefManager prefManager;

    public static ResourcesFragment newInstance() {
        return new ResourcesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resources_fragment, container, false);
        recyclerView = view.findViewById(R.id.rv_resources);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        prefManager = new PrefManager(getActivity());

        adapter = new ResourcesRvAdapter(getActivity(), this);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Log.d("course_id",CoursesActivity.courseId+"");
        if (CoursesActivity.courseId!=-1){
            mViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),
                    CoursesActivity.courseId,
                    10,
                    rvCurrentPage);
            rvCurrentPage+=1;
        }

        mViewModel.getTopicsResponseModelLiveData().observe(getActivity(), new Observer<TopicsResponseModel>() {
            @Override
            public void onChanged(final TopicsResponseModel topicsResponseModel) {
                if (topicsResponseModel!=null){
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                            rvLastPage = topicsResponseModel.getLast_page();

                            if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage<=rvLastPage){
                                isScrolling = false;
                                mViewModel.getCourseById(prefManager.getString(Constants.JWTTOKEN),
                                        CoursesActivity.courseId,
                                        10,
                                        rvCurrentPage);
                                rvCurrentPage+=1;
                            }

                        }
                    });
                }
            }
        });

       mViewModel.getLiveData(CoursesActivity.courseId).observe(getActivity(), new Observer<List<TopicsModel>>() {
           @Override
           public void onChanged(List<TopicsModel> topicsModels) {
               adapter.setTopics(topicsModels);
               adapter.notifyDataSetChanged();
           }
       });

    }

    @Override
    public void onResourceClicked(TopicsModel topicsModel) {
        Intent intent = new Intent(getActivity(), ResourcesActivity.class);
        intent.putExtra(Constants.TOPICID,topicsModel.getId());
        startActivity(intent);
    }
}
