package com.prepare.prepareurself.utils.youtubeplaylistapi.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;
    //VideoResources v1;
    String videoCode = "";
    String title = "";
    String decription = "";
    int projectId = -1;
    String playlistId = "";

    private TextView tvTitle, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        youTubePlayerView = findViewById(R.id.youtube_playerview);
        tvTitle = findViewById(R.id.tv_youtube_title);
        tvDescription = findViewById(R.id.tv_youtube_description);

        Intent intent = getIntent();

        if (intent.getIntExtra(Constants.PROJECTID, -1)!=-1){
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            projectId = intent.getIntExtra(Constants.PROJECTID,-1);

            tvTitle.setText(title);
            tvDescription.setText(decription);

        }else if (intent.getStringExtra(Constants.VideoItemWrapperPlaylistId)!=null){
            playlistId = intent.getStringExtra(Constants.VideoItemWrapperPlaylistId);
        }
        youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){
            if (!TextUtils.isEmpty(videoCode)){
                youTubePlayer.loadVideo(videoCode);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            }else if (!TextUtils.isEmpty(playlistId)){
                youTubePlayer.loadPlaylist(playlistId);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            } else{
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

}
