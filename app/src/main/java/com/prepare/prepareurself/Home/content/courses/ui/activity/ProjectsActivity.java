package com.prepare.prepareurself.Home.content.courses.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.LoginFilter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.Home.content.courses.ui.adapters.PlaylistVideosRvAdapter;
import com.prepare.prepareurself.Home.content.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.YoutubePlaylistResponseModel;

import java.util.List;

public class ProjectsActivity extends AppCompatActivity{

    int projectId = -1;
    private ProjectsViewModel viewModel;
    private TextView tvProjectTitle, tvProjectDescription;
    private ImageView imageProject;
    private RecyclerView recyclerView;
    private PlaylistVideosRvAdapter adapter;
    private Boolean isScrolling = false;
    private int rvCurrentItems, rvTotalItems, rvScrolledOutItems, rvLastPage;
    private String rvCurrentPageToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);

        tvProjectTitle = findViewById(R.id.tv_project_title);
        tvProjectDescription = findViewById(R.id.tv_project_decription);
        imageProject = findViewById(R.id.image_project);
        recyclerView = findViewById(R.id.rv_prjects_videos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        projectId = intent.getIntExtra(Constants.PROJECTID,-1);

        if (projectId != -1){
            viewModel.getProjectById(projectId).observe(this, new Observer<ProjectsModel>() {
                @Override
                public void onChanged(ProjectsModel projectsModel) {
                    updateUIWithProject(projectsModel);
                }
            });
        }


    }

    private void updateUIWithProject(ProjectsModel projectsModel) {

        getSupportActionBar().setTitle("Project Level : " + projectsModel.getLevel().toUpperCase());

        Glide.with(this)
                .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
                .override(150,150)
                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_image_loading_error)
                .into(imageProject);

        tvProjectTitle.setText(projectsModel.getName());
        if (projectsModel.getDescription()!=null)
            tvProjectDescription.setText(Html.fromHtml(projectsModel.getDescription()));

        adapter = new PlaylistVideosRvAdapter(ProjectsActivity.this);
        final GridLayoutManager layoutManager = new GridLayoutManager(ProjectsActivity.this,2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        viewModel.fetchVideosFromPlaylist(rvCurrentPageToken,"PLnfB3OWST3K3lCSi_YUrBujaAs0bFt4tz");

        viewModel.getVideosFromPlaylist()
                .observe(this, new Observer<YoutubePlaylistResponseModel>() {
                    @Override
                    public void onChanged(final YoutubePlaylistResponseModel youtubePlaylistResponseModel) {

                        if (youtubePlaylistResponseModel!=null){

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

                                    rvCurrentPageToken = youtubePlaylistResponseModel.getNextPageToken();

                                    if (isScrolling && (rvCurrentItems + rvScrolledOutItems) == rvTotalItems && rvCurrentPageToken!=null){
                                        isScrolling = false;
                                        viewModel.fetchVideosFromPlaylist(rvCurrentPageToken,"PLnfB3OWST3K3lCSi_YUrBujaAs0bFt4tz");
                                    }

                                }
                            });

                        }

                    }
                });

        viewModel.getVideoContentsLiveData().observe(this, new Observer<List<VideoContentDetails>>() {
            @Override
            public void onChanged(List<VideoContentDetails> videoContentDetails) {
                adapter.setVideoContentDetails(videoContentDetails);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


