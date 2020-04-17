package com.prepare.prepareurself.Home.content.resources.youtubevideoplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.io.UnsupportedEncodingException;
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
    String bitmapUriString="";
    Uri bitmapUri;

    private ResourcesDbRepository resourcesDbRepository;

    private TextView tvTitle,tvDescription;
    private RelatedVideosRvAdapter adapter;
    private ImageView imageView;
    private Boolean isDeepLinked = false;

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
        imageView = findViewById(R.id.img_share_youtube_video);

        Intent intent = getIntent();

        if (intent.getData()!=null){
            try {
                String tempData = intent.getData().toString().split("video_resource")[1];
                videoCode = Utility.base64DecodeForString(tempData);
                youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);
                isDeepLinked = true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            resourceId = intent.getIntExtra(Constants.RESOURCEID, -1);
            videoTitle = intent.getStringExtra(Constants.VIDEOTITLE);
            videoDescription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            videoTopicId = intent.getIntExtra(Constants.TOPICID, -1);
            bitmapUriString = intent.getStringExtra(Constants.BITMAPURI);
            bitmapUri = Uri.parse(bitmapUriString);
        }


        tvTitle.setText(videoTitle);
        tvDescription.setText(videoDescription);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String encodedVideoCode = Utility.base64EncodeForString(videoCode);
//                    String encodeVideoTitle = Utility.base64EncodeForString(videoTitle);
//                    String encodedDecription = Utility.base64EncodeForString(videoDescription);
//                    String encodedBitmapString = Utility.base64EncodeForString(videoDescription);

                    String text = videoTitle + "\n"
                            + "http://prepareurself.tk/video_resource/"
                            + encodedVideoCode;
//                            + encodeVideoTitle + "/"
//                            + encodedDecription + "/"
//                            + encodedBitmapString;

                    //Utility.shareContent(getApplicationContext(),bitmapUri,text);
                    final Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent,"Share Via"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvRelatedVideos.setLayoutManager(layoutManager);
        rvRelatedVideos.setAdapter(adapter);

        //List<ResourceModel> resourceModels = resourcesDbRepository.getResourceResourcesExcept(videoTopicId,Constants.VIDEO, resourceId).getValue();
        List<ResourceModel> resourceModels = resourcesDbRepository.getResourcesByID(videoTopicId,Constants.VIDEO).getValue();

        adapter.setResources(resourceModels);
        adapter.notifyDataSetChanged();



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
