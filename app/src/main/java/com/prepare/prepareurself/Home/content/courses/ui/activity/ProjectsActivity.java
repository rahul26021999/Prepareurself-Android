package com.prepare.prepareurself.Home.content.courses.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.YoutubePlaylistResponseModel;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity{

    int projectId = -1;
    private ProjectsViewModel viewModel;
    private TextView tvProjectTitle, tvProjectDescription;
    private ImageView imageProject;
    private RecyclerView recyclerView;
    private PlaylistVideosRvAdapter adapter;
    private String rvNextPageToken = "";
    String playlist;
    private TextView tvLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);

        tvProjectTitle = findViewById(R.id.tv_project_title);
        tvProjectDescription = findViewById(R.id.tv_project_decription);
        imageProject = findViewById(R.id.image_project);
        recyclerView = findViewById(R.id.rv_prjects_videos);
        tvLoading = findViewById(R.id.tv_loading_project);

        recyclerView.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);

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

        if (projectsModel.getPlaylist()!=null) {
            playlist = Utility.getVideoPlaylistId(projectsModel.getPlaylist());

            adapter = new PlaylistVideosRvAdapter(ProjectsActivity.this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(ProjectsActivity.this, RecyclerView.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);


            getVideos(rvNextPageToken, playlist);

            viewModel.getVideoContentsLiveData(playlist).observe(ProjectsActivity.this, new Observer<List<VideoItemWrapper>>() {
                @Override
                public void onChanged(List<VideoItemWrapper> videoContentDetails) {
                    tvLoading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setVideoContentDetails(videoContentDetails);
                    adapter.notifyDataSetChanged();
                }
            });

        }

    }

    public void getVideos(String token, final String playlist){
        viewModel.fetchVideosFromPlaylist(token,playlist).observe(this, new Observer<YoutubePlaylistResponseModel>() {
            @Override
            public void onChanged(YoutubePlaylistResponseModel youtubePlaylistResponseModel) {
                if (youtubePlaylistResponseModel!=null){
                    if (youtubePlaylistResponseModel.getNextPageToken()!=null)
                        getVideos(youtubePlaylistResponseModel.getNextPageToken(),playlist);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    tvLoading.setVisibility(View.VISIBLE);
                    tvLoading.setText("Playlist Not found");
                }
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


