package com.prepare.prepareurself.Home.content.resources.youtubevideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.prepare.prepareurself.Home.content.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.Home.content.resources.ui.adapter.RelatedVideosRvAdapter;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;
    //VideoResources v1;
    String videoCode = "";
    int resourceId, videoTopicId ;
    String videoTitle="";
    String videoDescription="";
    RecyclerView rvRelatedVideos;

    private ResourcesDbRepository resourcesDbRepository;

    private TextView tvTitle,tvDescription;
    private RelatedVideosRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        resourcesDbRepository = new ResourcesDbRepository(getApplication());
        adapter = new RelatedVideosRvAdapter(this);

        youTubePlayerView = findViewById(R.id.youtube_playerview);
        tvTitle = findViewById(R.id.tv_youtube_title);
        tvDescription = findViewById(R.id.tv_youtube_description);
        rvRelatedVideos = findViewById(R.id.rv_relatedvideos_youtube);

        Intent intent = getIntent();

        videoCode = intent.getStringExtra(Constants.VIDEOCODE);
        resourceId = intent.getIntExtra(Constants.RESOURCEID, -1);
        videoTitle = intent.getStringExtra(Constants.VIDEOTITLE);
        videoDescription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
        videoTopicId = intent.getIntExtra(Constants.TOPICID, -1);

        tvTitle.setText(videoTitle);
        tvDescription.setText(videoDescription);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvRelatedVideos.setLayoutManager(layoutManager);
        rvRelatedVideos.setAdapter(adapter);

        //List<ResourceModel> resourceModels = resourcesDbRepository.getResourceResourcesExcept(videoTopicId,Constants.VIDEO, resourceId).getValue();
        List<ResourceModel> resourceModels = resourcesDbRepository.getResourcesByID(videoTopicId,Constants.VIDEO).getValue();

        adapter.setResources(resourceModels);
        adapter.notifyDataSetChanged();

//        v1 = new VideoResources();
//        v1.setVideoId("1");
//        v1.setVideoTitle("This is Video 1");
//        v1.setVideoCode("X2COHLCv0eQ");
//        v1.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){
            if (!TextUtils.isEmpty(videoCode)){
                youTubePlayer.loadVideo(videoCode);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            }else{
                Utility.showToast(this,Constants.SOMETHINGWENTWRONG);
            }

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Utility.showToast(this,errorMessage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);
    }
}
