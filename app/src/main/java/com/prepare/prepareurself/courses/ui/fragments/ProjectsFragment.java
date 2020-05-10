package com.prepare.prepareurself.courses.ui.fragments;

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

import com.prepare.prepareurself.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.courses.ui.adapters.ProjectsRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.io.IOException;
import java.util.List;

public class ProjectsFragment extends Fragment implements ProjectsRvAdapter.ProjectsRvInteractor {

    private ProjectsViewModel mViewModel;
    private RecyclerView recyclerView;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private TextView tvComingSoon;
    private ProjectsRvAdapter adapter;

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.projects_fragment, container, false);
        recyclerView = view.findViewById(R.id.rv_projects);
        tvComingSoon = view.findViewById(R.id.tv_coming_soon);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);
        prefManager = new PrefManager(getActivity());

        if(CoursesActivity.courseId!=-1){
            mViewModel.fetchProjects(prefManager.getString(Constants.JWTTOKEN),
                    CoursesActivity.courseId,
                    "",
                    10,
                    rvCurrentPage);
            rvCurrentPage+=1;
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ProjectsRvAdapter(getActivity(), this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(),R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        mViewModel.getProjectResponseMutableLiveData().observe(getActivity(), new Observer<ProjectResponse>() {
            @Override
            public void onChanged(final ProjectResponse projectResponse) {
                if (projectResponse!=null){
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

                            rvLastPage = projectResponse.getLast_page();

                            if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPage <= rvLastPage){
                                isScrolling = false;
                                mViewModel.fetchProjects(prefManager.getString(Constants.JWTTOKEN),
                                        CoursesActivity.courseId,
                                        "",
                                        10,
                                        rvCurrentPage);
                                rvCurrentPage+=1;

                            }

                        }
                    });
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getProjectByCourseId(CoursesActivity.courseId).observe(getActivity(), new Observer<List<ProjectsModel>>() {
            @Override
            public void onChanged(List<ProjectsModel> projectsModels) {
                if (projectsModels!=null && !projectsModels.isEmpty()){
                    tvComingSoon.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setProjects(projectsModels);
                    adapter.notifyDataSetChanged();
                }else{
                    tvComingSoon.setText("Coming soon...");
                    tvComingSoon.setVisibility(View.VISIBLE);

                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
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
    public void onProjectLiked(final ProjectsModel projectsModel, final int liked) {
        mViewModel.likeProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId(), liked)
                .observe(getActivity(), new Observer<ResourceLikesResponse>() {
                    @Override
                    public void onChanged(ResourceLikesResponse resourceLikesResponse) {
                        if (resourceLikesResponse!=null){
                            if (!resourceLikesResponse.getSuccess()){
                                Utility.showToast(getActivity(),"Unable to like at the moment");
                            }else{
                                if (liked == 0){
                                    projectsModel.setLike(1);
                                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()+1);
                                }else if (liked == 1){
                                    projectsModel.setLike(0);
                                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()-1);
                                }
                                mViewModel.saveProject(projectsModel);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
