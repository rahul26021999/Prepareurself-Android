package com.prepare.prepareurself.utils.youtubeplaylistapi.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.like.LikeButton;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.viewmodel.VideoViewModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, PlaylistItemAdapter.PlaylistItemListener, ViewModelStoreOwner {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;
    //VideoResources v1;
    String videoCode = "";
    String title = "";
    String decription = "";
    int projectId = -1;
    String playlistId = "";
    LikeButton likeButton;
    ImageView imageViewShare;
    ExpandableLayout expandableLayout;
    ImageView imageDown;
    RecyclerView playlistItemRecyclerView;

    private VideoViewModel videoViewModel;

    private TextView tvTitle, tvDescription;

    @Nullable
    private ViewModelStore viewModelStore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoViewModel = getViewModelProvider().get(VideoViewModel.class);

        youTubePlayerView = findViewById(R.id.youtube_playerview);
        tvTitle = findViewById(R.id.tv_youtube_title);
        tvDescription = findViewById(R.id.tv_youtube_description);
        imageViewShare = findViewById(R.id.img_share_youtube_video);
        likeButton = findViewById(R.id.like_button_youtube);
        imageDown = findViewById(R.id.img_down_video);
        expandableLayout = findViewById(R.id.expandable_layout);
        playlistItemRecyclerView = findViewById(R.id.rv_playlist_item_player);

        Intent intent = getIntent();

        if (intent.getIntExtra(Constants.PROJECTID, -1)!=-1){
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            projectId = intent.getIntExtra(Constants.PROJECTID,-1);

            tvTitle.setText(title);
            tvDescription.setText(decription);

            playlistItemRecyclerView.setVisibility(View.GONE);

        }else if (intent.getStringExtra(Constants.VideoItemWrapperPlaylistId)!=null){
            imageViewShare.setVisibility(View.GONE);
            likeButton.setVisibility(View.GONE);
            playlistId = intent.getStringExtra(Constants.VideoItemWrapperPlaylistId);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            tvTitle.setText(title);
            tvDescription.setText(decription);

            playlistItemRecyclerView.setVisibility(View.VISIBLE);

            final PlaylistItemAdapter playlistItemAdapter = new PlaylistItemAdapter(getApplicationContext(),this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            playlistItemRecyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration decoration = new DividerItemDecoration(this,R.drawable.theory_resource_divider);
            playlistItemRecyclerView.addItemDecoration(decoration);
            playlistItemRecyclerView.setAdapter(playlistItemAdapter);

            videoViewModel.getListLiveData(playlistId).observeForever(new Observer<List<VideoItemWrapper>>() {
                @Override
                public void onChanged(List<VideoItemWrapper> videoItemWrappers) {
                    playlistItemAdapter.setVideoItemWrappers(videoItemWrappers);
                    playlistItemAdapter.notifyDataSetChanged();
                }
            });

        }
        youTubePlayerView.initialize(Constants.YOUTUBE_PLAYER_API_KEY,this);

        imageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expandableLayout.isExpanded()){
                    imageDown.animate().rotation(180).start();
                    expandableLayout.expand(true);
                }else{
                    imageDown.animate().rotation(0).start();
                    expandableLayout.collapse(true);
                }
            }
        });

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

    @Override
    public void onItemClicked(VideoItemWrapper videoItemWrapper) {

    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        Object nonConfigurationInstance = getLastNonConfigurationInstance();
        if (nonConfigurationInstance instanceof ViewModelStore) {
            viewModelStore = (ViewModelStore) nonConfigurationInstance;
        }
        if (viewModelStore == null) {
            viewModelStore = new ViewModelStore();
        }
        return viewModelStore;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return viewModelStore;
    }

    public ViewModelProvider getViewModelProvider() {
        ViewModelProvider.Factory factory =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(getViewModelStore(), factory);
    }
}
