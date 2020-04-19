package com.prepare.prepareurself.Home.content.courses.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

import com.prepare.prepareurself.Home.content.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.Home.content.courses.ui.activity.CoursesActivity;
import com.prepare.prepareurself.Home.content.courses.ui.adapters.ProjectsRvAdapter;
import com.prepare.prepareurself.Home.content.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.Home.content.resources.ui.activity.ResourcesActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class ProjectsFragment extends Fragment implements ProjectsRvAdapter.ProjectsRvInteractor {

    private ProjectsViewModel mViewModel;
    private RecyclerView recyclerView;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.projects_fragment, container, false);
        recyclerView = view.findViewById(R.id.rv_projects);

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
        final ProjectsRvAdapter adapter = new ProjectsRvAdapter(getActivity(), this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(),R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        mViewModel.getProjectResponseMutableLiveData().observe(getActivity(), new Observer<ProjectResponse>() {
            @Override
            public void onChanged(final ProjectResponse projectResponse) {
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
        });

        mViewModel.getListLiveData().observe(getActivity(), new Observer<List<ProjectsModel>>() {
            @Override
            public void onChanged(List<ProjectsModel> projectsModels) {
                adapter.setProjects(projectsModels);
                adapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {

    }
}
