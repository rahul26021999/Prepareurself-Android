package com.example.prepareurself.Home.content.resources.youtubevideoplayer;

import android.os.Bundle;

import com.example.prepareurself.Home.content.resources.model.VideoResources;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.Utility;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;
    VideoResources v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        youTubePlayerView = findViewById(R.id.youtube_playerview);

        youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);

        v1 = new VideoResources();
        v1.setVideoId("1");
        v1.setVideoTitle("This is Video 1");
        v1.setVideoCode("X2COHLCv0eQ");
        v1.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){
            youTubePlayer.loadVideo(v1.getVideoCode());
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
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
}
