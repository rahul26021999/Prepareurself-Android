package com.prepare.prepareurself.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.ui.adapters.PlaylistVideosRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.SingleVIdeoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubePlaylistResponseModel;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import java.util.List;

public class ProjectsActivity extends AppCompatActivity implements PlaylistVideosRvAdapter.PlaylistVideoRvInteractor {

    int projectId = -1;
    private ProjectsViewModel viewModel;
    private TextView tvProjectTitle, tvProjectDescription;
    private ImageView imageProject;
    private RecyclerView recyclerView;
    private PlaylistVideosRvAdapter adapter;
    private String rvNextPageToken = "";
    String playlist="";
    private TextView tvLoading;
    private CardView cardImageView;
    private ImageView videoImageView;
    private TextView tvCardVideoTitle, tvReferenceHeader, tvViewPlaylist;
    private String videoTitle = "", videoDescription="", videoCode ="";
    private String projectTitle = "";
    private String projectDescription = "";
    private PrefManager prefManager;

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
        cardImageView = findViewById(R.id.card_project_image);
        videoImageView = findViewById(R.id.project_video_image_view);
        tvCardVideoTitle = findViewById(R.id.tv_card_video_project);
        tvReferenceHeader = findViewById(R.id.tv_reference_heading);
        tvViewPlaylist = findViewById(R.id.tv_viewfullplaylist);

        recyclerView.setVisibility(View.GONE);
        cardImageView.setVisibility(View.GONE);
        tvViewPlaylist.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);

        prefManager = new PrefManager(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        if (intent.getData()!=null){
            projectId = Integer.parseInt(intent.getData().toString().split("&id=")[1]);
        }else{
            projectId = intent.getIntExtra(Constants.PROJECTID,-1);
        }


        if (projectId != -1){
            viewModel.getProjectById(projectId).observe(this, new Observer<ProjectsModel>() {
                @Override
                public void onChanged(ProjectsModel projectsModel) {
                    if (projectsModel!=null){
                        if (projectsModel.getView()==0){
                            viewModel.viewProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId());
                        }
                        updateUIWithProject(projectsModel);
                    }
                }
            });
        }

        tvViewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvViewPlaylist.getVisibility() == View.VISIBLE){
                    if (!TextUtils.isEmpty(playlist)){
                        Intent playlistIntent = new Intent(ProjectsActivity.this, VideoActivity.class);
                        playlistIntent.putExtra(Constants.PLAYLIST,true);
                        playlistIntent.putExtra(Constants.VideoItemWrapperPlaylistId,playlist);
                        playlistIntent.putExtra(Constants.VIDEOTITLE,projectTitle);
                        playlistIntent.putExtra(Constants.VIDEODESCRIPTION,projectDescription);
                        startActivity(playlistIntent);
                    }
                }
            }
        });


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

        projectTitle = projectsModel.getName();
        tvProjectTitle.setText(projectTitle);
        if (projectsModel.getDescription()!=null){
            tvProjectDescription.setText(Html.fromHtml(projectsModel.getDescription()));
            projectDescription = Html.fromHtml(projectsModel.getDescription()).toString();
        }

        if (projectsModel.getPlaylist()!=null) {

            tvReferenceHeader.setText("Playlist");

            playlist = Utility.getVideoPlaylistId(projectsModel.getPlaylist());

            adapter = new PlaylistVideosRvAdapter(ProjectsActivity.this, this);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(ProjectsActivity.this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);


            getVideos(rvNextPageToken, playlist, projectsModel);


        }else if (projectsModel.getLink()!=null){

            loadSingleLink(projectsModel);
        }

    }

    private void loadSingleLink(ProjectsModel projectsModel) {

        tvReferenceHeader.setText("Video");

        recyclerView.setVisibility(View.GONE);
        tvViewPlaylist.setVisibility(View.GONE);
        cardImageView.setVisibility(View.VISIBLE);
        final String videoCode = Utility.getVideoCode(projectsModel.getLink());
        loadCardImageViewWithVideo(videoCode);


        cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(videoTitle)){
                    Intent intent = new Intent(ProjectsActivity.this,VideoActivity.class);
                    intent.putExtra(Constants.VIDEOCODE,videoCode);
                    intent.putExtra(Constants.VIDEOTITLE, videoTitle);
                    intent.putExtra(Constants.VIDEODESCRIPTION, videoDescription);
                    intent.putExtra(Constants.SINGLEVIDEO,true);
                    startActivity(intent);
                }
                Log.d("click_debug","jksbvjkd");
            }
        });
    }

    private void loadCardImageViewWithVideo(final String videoCode) {

        viewModel.fetchVideoDetails(videoCode).observe(this, new Observer<SingleVIdeoItemWrapper>() {
            @Override
            public void onChanged(final SingleVIdeoItemWrapper singleVIdeoItemWrapper) {

                if (singleVIdeoItemWrapper!=null){
                    tvLoading.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tvViewPlaylist.setVisibility(View.GONE);
                    tvCardVideoTitle.setText(singleVIdeoItemWrapper.getSnippet().getTitle());
                    videoTitle = singleVIdeoItemWrapper.getSnippet().getTitle();
                    videoDescription = singleVIdeoItemWrapper.getSnippet().getDescription();
                    Glide.with(ProjectsActivity.this)
                            .load(singleVIdeoItemWrapper.getSnippet().getThumbnails().getMaxres().getUrl())
                            .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.ic_image_loading_error)
                            .into(videoImageView);

                }
            }
        });

    }

    public void getVideos(String token, final String playlist, final ProjectsModel projectsModel){
        viewModel.fetchVideosFromPlaylist(token,playlist).observe(this, new Observer<YoutubePlaylistResponseModel>() {
            @Override
            public void onChanged(YoutubePlaylistResponseModel youtubePlaylistResponseModel) {
                if (youtubePlaylistResponseModel!=null){
                    if (youtubePlaylistResponseModel.getNextPageToken()!=null)
                        getVideos(youtubePlaylistResponseModel.getNextPageToken(),playlist, projectsModel);
                    else{
                        viewModel.getVideoContentsLiveData(playlist).observe(ProjectsActivity.this, new Observer<List<VideoItemWrapper>>() {
                            @Override
                            public void onChanged(List<VideoItemWrapper> videoContentDetails) {
                                tvLoading.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                tvViewPlaylist.setVisibility(View.VISIBLE);
                                adapter.setVideoContentDetails(videoContentDetails);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }else if (projectsModel.getLink()!=null){
                    loadSingleLink(projectsModel);
                }else{
                    cardImageView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tvLoading.setVisibility(View.VISIBLE);
                    tvViewPlaylist.setVisibility(View.GONE);
                    tvLoading.setText("Playlist Not found");
                }
            }
        });
    }

    @Override
    public void onVideoClicked(VideoItemWrapper videoItemWrapper, String videoCode) {
        Intent intent = new Intent(ProjectsActivity.this, VideoActivity.class);
        intent.putExtra(Constants.SINGLEVIDEOOFPLAYLIST,true);
        intent.putExtra(Constants.VIDEOINDEX, videoItemWrapper.getSnippet().getPosition());
        intent.putExtra(Constants.VideoItemWrapperPlaylistId,videoItemWrapper.getSnippet().getPlaylistId());
        intent.putExtra(Constants.VIDEOTITLE,projectTitle);
        intent.putExtra(Constants.VIDEODESCRIPTION,projectDescription);
        startActivity(intent);
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


