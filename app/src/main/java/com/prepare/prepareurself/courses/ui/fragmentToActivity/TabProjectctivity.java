package com.prepare.prepareurself.courses.ui.fragmentToActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.courses.ui.adapters.ProjectsRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.io.IOException;
import java.util.List;

public class TabProjectctivity extends BaseActivity implements ProjectsRvAdapter.ProjectsRvInteractor {
    private ProjectsViewModel mViewModel;
    private RecyclerView recyclerView;
    private PrefManager prefManager;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage, rvCurrentPage=1;
    private TextView tvComingSoon;
    private ProjectsRvAdapter adapter;
    private int courseId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_projectctivity);
        recyclerView = findViewById(R.id.rv_projects);
        tvComingSoon = findViewById(R.id.tv_coming_soon);
        mViewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);
        prefManager = new PrefManager(this);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(Constants.COURSEID,-1);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProjectsRvAdapter(TabProjectctivity.this, this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,R.drawable.theory_resource_divider);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        if(courseId!=-1){
            mViewModel.fetchProjects(prefManager.getString(Constants.JWTTOKEN),
                    courseId,
                    "",
                    10,
                    rvCurrentPage);
            rvCurrentPage+=1;
        }


        mViewModel.getProjectByCourseId(courseId).observe(this, new Observer<List<ProjectsModel>>() {
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

        mViewModel.getProjectResponseMutableLiveData().observe(this, new Observer<ProjectResponse>() {
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
                                        courseId,
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

    }


    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
        Intent intent = new Intent(TabProjectctivity.this, ProjectsActivity.class);
        intent.putExtra(Constants.PROJECTID,projectsModel.getId());
        startActivity(intent);
    }

    @Override
    public void onProjectShared(Bitmap bitmap, String text) {
        try {
            Uri bitmapUri = Utility.getUriOfBitmap(bitmap, this);
            Utility.shareContent(this,bitmapUri,text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProjectLiked(final ProjectsModel projectsModel, final int liked) {
        mViewModel.likeProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId(), liked)
                .observe(this, new Observer<ResourceLikesResponse>() {
                    @Override
                    public void onChanged(ResourceLikesResponse resourceLikesResponse) {
                        if (resourceLikesResponse!=null){
                            if (!resourceLikesResponse.getSuccess()){
                                Utility.showToast(TabProjectctivity.this,"Unable to like at the moment");
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
                                LikedProjectsModel likedProjectsModel = new LikedProjectsModel();
                                likedProjectsModel.setId(projectsModel.getId());
                                likedProjectsModel.setName(projectsModel.getName());
                                likedProjectsModel.setDescription(projectsModel.getDescription());
                                likedProjectsModel.setType(projectsModel.getType());
                                likedProjectsModel.setLevel(projectsModel.getLevel());
                                likedProjectsModel.setImage_url(projectsModel.getImage_url());
                                likedProjectsModel.setLink(projectsModel.getLink());
                                likedProjectsModel.setPlaylist(projectsModel.getPlaylist());
                                likedProjectsModel.setCourse_id(projectsModel.getCourse_id());
                                likedProjectsModel.setAdmin_id(projectsModel.getAdmin_id());
                                likedProjectsModel.setCreated_at(projectsModel.getCreated_at());
                                likedProjectsModel.setUpdated_at(projectsModel.getUpdated_at());
                                likedProjectsModel.setLike(projectsModel.getLike());
                                likedProjectsModel.setTotal_likes(projectsModel.getTotal_likes());
                                likedProjectsModel.setView(projectsModel.getView());
                                likedProjectsModel.setSequence(projectsModel.getSequence());

                                if (liked == 1){
                                    mViewModel.deleteLikedProject(likedProjectsModel);
                                }else if (liked == 0){
                                    mViewModel.insertLikedProject(likedProjectsModel);
                                }

                            }
                        }
                    }
                });
    }
    }
