package com.prepare.prepareurself.resources.youtubevideoplayer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.MenuItem;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.ui.adapter.RelatedVideosRvAdapter;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.utils.FullScreenHelper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.viewmodel.YoutubeViewModel;

import java.io.UnsupportedEncodingException;

public class YoutubePlayerActivity extends AppCompatActivity {

    YouTubePlayerView youTubePlayerView;
    //VideoResources v1;
    String videoCode = "";
    int resourceId =-1, videoTopicId ;
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

    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);

    private String videoItemWrapperId = "";
    private String videoItemWrapperPlaylistId = "";
    private int projectId =-1;

    private YoutubeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        viewModel = ViewModelProviders.of(this).get(YoutubeViewModel.class);

        resourcesDbRepository = new ResourcesDbRepository(getApplication());
        adapter = new RelatedVideosRvAdapter(this);

        youTubePlayerView = findViewById(R.id.youtube_playerview);
        tvTitle = findViewById(R.id.tv_youtube_title);
        tvDescription = findViewById(R.id.tv_youtube_description);
        rvRelatedVideos = findViewById(R.id.rv_relatedvideos_youtube);
        imageView = findViewById(R.id.img_share_youtube_video);

        Intent intent = getIntent();

        if (intent.getData()!=null){
//            try {
//                String tempData = intent.getData().toString().split("video_resource")[1];
//                resourceId = Utility.base64DecodeForInt(tempData);
//              //  youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);
//                setupUiWithVideoCode(resourceId);
//
//                isDeepLinked = true;
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            Log.d("deeplink_debug","youtube avtivity : "+getIntent().getData()+"");

        }else if (intent.getIntExtra(Constants.PROJECTID,-1)!=-1){
            videoItemWrapperId = intent.getStringExtra(Constants.VideoItemWrapperId);
            videoItemWrapperPlaylistId = intent.getStringExtra(Constants.VideoItemWrapperPlaylistId);
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            projectId = intent.getIntExtra(Constants.PROJECTID, -1);

            setupUiWithVideoWrapperView(videoCode,videoItemWrapperPlaylistId);

        }else if (intent.getIntExtra(Constants.RESOURCEID,-1)!=-1){
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            resourceId = intent.getIntExtra(Constants.RESOURCEID, -1);
            bitmapUriString = intent.getStringExtra(Constants.BITMAPURI);
            bitmapUri = Uri.parse(bitmapUriString);

            if (resourceId!=-1){
                setupUiWithResource(resourceId, videoCode);
            }

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String text = "";

                    if (resourceId!=-1){
                        String encodedVideoCode = Utility.base64EncodeForInt(resourceId);

                        text = videoTitle + "\n"
                                + "prepareurself.tk/prepareurself_course/"
                                + encodedVideoCode;
                    }else if (projectId!=-1){
                        String encodedVideoCode = Utility.base64EncodeForInt(projectId);

                        text = videoTitle + "\n"
                                + "prepareurself.tk/prepareurself_course/"
                                + encodedVideoCode;
                    }

                    final Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    intent.setType("image/png");
                    if (!TextUtils.isEmpty(text))
                        startActivity(Intent.createChooser(intent,"Share Via"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

   //     initYouTubePlayerView(videoCode);

//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        rvRelatedVideos.setLayoutManager(layoutManager);
//        rvRelatedVideos.setAdapter(adapter);
//
//        //List<ResourceModel> resourceModels = resourcesDbRepository.getResourceResourcesExcept(videoTopicId,Constants.VIDEO, resourceId).getValue();
//        List<ResourceModel> resourceModels = resourcesDbRepository.getResourcesByID(videoTopicId,Constants.VIDEO).getValue();
//
//        adapter.setResources(resourceModels);
//        adapter.notifyDataSetChanged();



    }

    private void setupUiWithVideoCode(int resourceId) {

        viewModel.getResourceByResourceId(resourceId,Constants.VIDEO)
                .observe(this, new Observer<ResourceModel>() {
                    @Override
                    public void onChanged(ResourceModel resourceModel) {
                        if (resourceModel!=null){
                            String videoCode = Utility.getVideoCode(resourceModel.getLink());
                            initYouTubePlayerView(videoCode);
                            tvTitle.setText(resourceModel.getTitle());
                            tvDescription.setText(resourceModel.getDescription());
                        }else{
                            tvTitle.setText("Invalid URL");
                            tvDescription.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void setupUiWithResource(int resourceId, String videoCode) {

        initYouTubePlayerView(videoCode);

        viewModel.getResourceByResourceId(resourceId,Constants.VIDEO)
                .observe(this, new Observer<ResourceModel>() {
                    @Override
                    public void onChanged(ResourceModel resourceModel) {
                        tvTitle.setText(resourceModel.getTitle());
                        tvDescription.setText(resourceModel.getDescription());
                    }
                });
    }

    private void setupUiWithVideoWrapperView(String videoCode, String videoItemWrapperPlaylistId) {
        initYouTubePlayerView(videoCode);

        viewModel.getVideoItemWrapperLiveData(videoCode,videoItemWrapperPlaylistId)
                .observe(this, new Observer<VideoItemWrapper>() {
                    @Override
                    public void onChanged(VideoItemWrapper videoItemWrapper) {
                        tvTitle.setText(videoItemWrapper.getSnippet().getTitle());
                        tvDescription.setText(videoItemWrapper.getSnippet().getDescription());
                    }
                });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
    }

    private void initYouTubePlayerView(final String videoCode) {
        //initPlayerMenu();

        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        videoCode,
                        0f
                );

                addFullScreenListenerToPlayer();
                setPlayNextVideoButtonClickListener(youTubePlayer);
            }
        });
    }

    private void initPlayerMenu() {
        youTubePlayerView.getPlayerUiController()
                .showMenuButton(true)
                .getMenu()
                .addItem(new MenuItem("menu item1", R.drawable.ic_action_back_arror_white,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(YoutubePlayerActivity.this, "item1 clicked", Toast.LENGTH_SHORT).show();
                            }
                        })
                ).addItem(new MenuItem("menu item2", R.drawable.ic_action_back_arror_white,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(YoutubePlayerActivity.this, "item1 clicked", Toast.LENGTH_SHORT).show();
                    }
                })
        ).addItem(new MenuItem("menu item no icon",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(YoutubePlayerActivity.this, "item no icon clicked", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void addFullScreenListenerToPlayer() {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();

               // addCustomActionsToPlayer();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();

             //   removeCustomActionsFromPlayer();
            }
        });
    }

    private void addCustomActionsToPlayer() {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_action_back_arror_white);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_action_back_arror_white);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(YoutubePlayerActivity.this, "custom action1 clicked", Toast.LENGTH_SHORT).show();
                    }
                });

        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(YoutubePlayerActivity.this, "custom action1 clicked", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
    }

    private void setPlayNextVideoButtonClickListener(final YouTubePlayer youTubePlayer) {
//        Button playNextVideoButton = findViewById(R.id.next_video_button);
//
//        playNextVideoButton.setOnClickListener(view ->
//                YouTubePlayerUtils.loadOrCueVideo(
//                        youTubePlayer, getLifecycle(),
//                        VideoIdsProvider.getNextVideoId(),0f
//                ));
    }


//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//        if (!b){
//            if (!TextUtils.isEmpty(videoCode)){
//                youTubePlayer.loadVideo(videoCode);
//                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//            }else{
//                Utility.showToast(this,Constants.SOMETHINGWENTWRONG);
//            }
//
//        }
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//        if (youTubeInitializationResult.isUserRecoverableError()) {
//            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
//        } else {
//            String errorMessage = String.format(
//                    getString(R.string.error_player), youTubeInitializationResult.toString());
//            Utility.showToast(this,errorMessage);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);
//    }
}
