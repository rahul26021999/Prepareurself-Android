package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.model.VideoShareResponseModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.viewmodel.VideoViewModel;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.io.UnsupportedEncodingException;
import java.util.List;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        PlaylistItemAdapter.PlaylistItemListener,
        ViewModelStoreOwner,
        RelatedVideosRvAdapter.RelatedRvListener, YouTubePlayer.OnFullscreenListener,
        LikedRelatedVideosRvAdapter.RelatedRvListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youTubePlayerView;
    //VideoResources v1;
    String videoCode = "";
    String title = "";
    String decription = "";
    int projectId = -1;
    String playlistId = "";
    ImageView imageViewShare;
    ExpandableLayout expandableLayout;
    ImageView imageDown;
    RecyclerView playlistItemRecyclerView;
    private int videoIndex = -1;
    private String bitmapUri;

    private VideoViewModel videoViewModel;

    private TextView tvTitle, tvDescription, tvPlaylst;
    private YouTubePlayer mYoutubePlayer;
    private PlaylistItemAdapter playlistItemAdapter;
    private int resourceId =-1;
    private int topicId = -1;
    private RecyclerView rvOtherVideos;
    private TextView tvRelatedVideosHeader;
    private PrefManager prefManager;
    private AdView mAdView, projectAdView;

    @Nullable
    private ViewModelStore viewModelStore = null;
    private boolean isFullscreen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoViewModel = getViewModelProvider().get(VideoViewModel.class);

        youTubePlayerView = findViewById(R.id.youtube_playerview);
        tvTitle = findViewById(R.id.tv_youtube_title);
        tvDescription = findViewById(R.id.tv_youtube_description);
        imageViewShare = findViewById(R.id.img_share_youtube_video);
        imageDown = findViewById(R.id.img_down_video);
        expandableLayout = findViewById(R.id.expandable_layout);
        playlistItemRecyclerView = findViewById(R.id.rv_playlist_item_player);
        rvOtherVideos = findViewById(R.id.rv_other_videos);
        tvRelatedVideosHeader = findViewById(R.id.tv_related_header);
        //fnd
        tvPlaylst= findViewById(R.id.tvPlaylst);
        mAdView=findViewById(R.id.adView);
        projectAdView = findViewById(R.id.adViewProject);


        prefManager = new PrefManager(this);

        Intent intent = getIntent();

        setGoogleAdd();

        if (intent.getBooleanExtra(Constants.SINGLEVIDEO, false)){

            hideShareAndLike();

            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);

            tvTitle.setText(title);
            tvDescription.setText(decription);

            projectAdView.setVisibility(View.GONE);
            mAdView.setVisibility(View.VISIBLE);

            playlistItemRecyclerView.setVisibility(View.GONE);
            rvOtherVideos.setVisibility(View.GONE);
            tvRelatedVideosHeader.setVisibility(View.GONE);
            tvPlaylst.setVisibility(View.GONE);
            //

        }else if (intent.getBooleanExtra(Constants.PLAYLIST,false)){
            hideShareAndLike();
            rvOtherVideos.setVisibility(View.GONE);
            playlistId = intent.getStringExtra(Constants.VideoItemWrapperPlaylistId);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            tvTitle.setText(title);
            tvDescription.setText(decription);

            playlistItemRecyclerView.setVisibility(View.VISIBLE);
            tvRelatedVideosHeader.setVisibility(View.GONE);
            tvPlaylst.setVisibility(View.VISIBLE);
            projectAdView.setVisibility(View.GONE);
            mAdView.setVisibility(View.GONE);

            playlistItemAdapter = new PlaylistItemAdapter(getApplicationContext(),this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            playlistItemRecyclerView.setLayoutManager(layoutManager);
            playlistItemRecyclerView.setAdapter(playlistItemAdapter);

            videoViewModel.getListLiveData(playlistId).observeForever(new Observer<List<VideoItemWrapper>>() {
                @Override
                public void onChanged(List<VideoItemWrapper> videoItemWrappers) {
                    playlistItemAdapter.setVideoItemWrappers(videoItemWrappers);
                    playlistItemAdapter.notifyDataSetChanged();
                }
            });

        }else if (intent.getBooleanExtra(Constants.SINGLEVIDEOOFPLAYLIST, false)){
            playlistId = intent.getStringExtra(Constants.VideoItemWrapperPlaylistId);
            videoIndex = intent.getIntExtra(Constants.VIDEOINDEX, -1);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            tvTitle.setText(title);
            tvDescription.setText(decription);

            hideShareAndLike();

            playlistItemRecyclerView.setVisibility(View.VISIBLE);
            rvOtherVideos.setVisibility(View.GONE);
            tvRelatedVideosHeader.setVisibility(View.GONE);
            tvPlaylst.setVisibility(View.VISIBLE);

            projectAdView.setVisibility(View.GONE);
            mAdView.setVisibility(View.GONE);

            playlistItemAdapter = new PlaylistItemAdapter(getApplicationContext(),this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            playlistItemRecyclerView.setLayoutManager(layoutManager);
            playlistItemRecyclerView.setAdapter(playlistItemAdapter);

            videoViewModel.getListLiveData(playlistId).observeForever(new Observer<List<VideoItemWrapper>>() {
                @Override
                public void onChanged(List<VideoItemWrapper> videoItemWrappers) {
                    playlistItemAdapter.setVideoItemWrappers(videoItemWrappers);
                    playlistItemAdapter.setSelectedPosition(videoIndex);
                    playlistItemAdapter.notifyDataSetChanged();
                }
            });

        }else if (intent.getBooleanExtra(Constants.RESOURCEVIDEO,false)){
            showShareAndLike();
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            bitmapUri = intent.getStringExtra(Constants.BITMAPURI);
            resourceId = intent.getIntExtra(Constants.RESOURCEID, -1);
            topicId = intent.getIntExtra(Constants.TOPICID,-1);

            tvTitle.setText(title);
            tvDescription.setText(decription);

            playlistItemRecyclerView.setVisibility(View.GONE);
            tvRelatedVideosHeader.setVisibility(View.VISIBLE);
            rvOtherVideos.setVisibility(View.VISIBLE);
            tvPlaylst.setVisibility(View.GONE);

            tvRelatedVideosHeader.setText("Related Videos");

            mAdView.setVisibility(View.VISIBLE);
            projectAdView.setVisibility(View.GONE);

            if (resourceId!=-1){
                videoViewModel.fetchResourceById(prefManager.getString(Constants.JWTTOKEN), resourceId);
                final RelatedVideosRvAdapter relatedVideosRvAdapter = new RelatedVideosRvAdapter(VideoActivity.this, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                rvOtherVideos.setLayoutManager(layoutManager);
                rvOtherVideos.setAdapter(relatedVideosRvAdapter);

                videoViewModel.getResourceExceptId(resourceId,Constants.VIDEO,topicId)
                        .observeForever(new Observer<List<ResourceModel>>() {
                            @Override
                            public void onChanged(List<ResourceModel> resourceModels) {
                                relatedVideosRvAdapter.setResources(resourceModels);
                                relatedVideosRvAdapter.notifyDataSetChanged();
                            }
                        });

                checkForViews(resourceId);

                imageViewShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(bitmapUri);
                        try {
                            String encodedId = Utility.base64EncodeForInt(resourceId);
                            String text = "Checkout our prepareurself app. "
                                    + "I found some best resources  from internet at one place and learning is so much fun now.\n"
                                    + "You can learn them too here :\n"+
                                    "prepareurself.in/resource/"+encodedId;
                            share(uri,text);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

        }else if (intent.getBooleanExtra(Constants.RESOURCEVIDEOLIKED,false)){
            showShareAndLike();
            videoCode = intent.getStringExtra(Constants.VIDEOCODE);
            title = intent.getStringExtra(Constants.VIDEOTITLE);
            decription = intent.getStringExtra(Constants.VIDEODESCRIPTION);
            bitmapUri = intent.getStringExtra(Constants.BITMAPURI);
            resourceId = intent.getIntExtra(Constants.RESOURCEID, -1);
            topicId = intent.getIntExtra(Constants.TOPICID,-1);

            tvTitle.setText(title);
            tvDescription.setText(decription);

            playlistItemRecyclerView.setVisibility(View.GONE);
            tvRelatedVideosHeader.setVisibility(View.VISIBLE);
            rvOtherVideos.setVisibility(View.VISIBLE);
            tvPlaylst.setVisibility(View.GONE);

            tvRelatedVideosHeader.setText("Other Liked Videos");

            mAdView.setVisibility(View.VISIBLE);
            projectAdView.setVisibility(View.GONE);

            checkForViews(resourceId);

            final LikedRelatedVideosRvAdapter relatedVideosRvAdapter = new LikedRelatedVideosRvAdapter(VideoActivity.this, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            rvOtherVideos.setLayoutManager(layoutManager);
            rvOtherVideos.setAdapter(relatedVideosRvAdapter);

            videoViewModel.getLikedResourceExceptId(resourceId,Constants.VIDEO)
                    .observeForever(new Observer<List<LikedResourcesModel>>() {
                        @Override
                        public void onChanged(List<LikedResourcesModel> resourceModels) {
                            relatedVideosRvAdapter.setResources(resourceModels);
                            relatedVideosRvAdapter.notifyDataSetChanged();
                        }
                    });

            imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(bitmapUri);
                    try {
                        String encodedId = Utility.base64EncodeForInt(resourceId);
                        String text = "Checkout our prepareurself app. "
                                + "I found some best resources  from internet at one place and learning is so much fun now.\n"
                                + "You can learn them too here :\n"+
                                "prepareurself.in/resource/"+encodedId;
                        share(uri,text);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });

        }else if (intent.getData()!=null){
            showShareAndLike();
            Log.d("deeplink_debug",intent.getData().toString());
            resourceId = Integer.parseInt(intent.getData().toString().split("&id=")[1]);
            if (!prefManager.getBoolean(Constants.ISLOGGEDIN)){
                Intent loginIntent = new Intent(VideoActivity.this, AuthenticationActivity.class);
                loginIntent.putExtra(Constants.RESOURCEID,resourceId);
                startActivity(loginIntent);
            }else{
                videoViewModel.getResourceFromRemote(prefManager.getString(Constants.JWTTOKEN),resourceId)
                        .observeForever(new Observer<VideoShareResponseModel>() {
                            @Override
                            public void onChanged(VideoShareResponseModel videoShareResponseModel) {
                                if (videoShareResponseModel!=null){
                                    if (videoShareResponseModel.getError_code() == 0){
                                        videoCode = Utility.getVideoCode(videoShareResponseModel.getResource().getLink());
                                        tvTitle.setText(videoShareResponseModel.getResource().getTitle());
                                        tvDescription.setText(videoShareResponseModel.getResource().getDescription());
                                    }else{
                                        tvTitle.setText("Invalid link");
                                        tvDescription.setText("");
                                    }
                                }else{
                                    Utility.showToast(VideoActivity.this,Constants.SOMETHINGWENTWRONG);
                                }
                            }
                        });
            }

            //topicId = Integer.parseInt(intent.getData().toString().split("&topicid=")[1]);
        }else if (intent.getBooleanExtra(Constants.DEEPSHAREVIDEOAFTERLOGIN, false)){
            showShareAndLike();
            resourceId = intent.getIntExtra(Constants.RESOURCEID, -1);
            videoViewModel.getResourceFromRemote(prefManager.getString(Constants.JWTTOKEN),resourceId)
                    .observeForever(new Observer<VideoShareResponseModel>() {
                        @Override
                        public void onChanged(VideoShareResponseModel videoShareResponseModel) {
                            if (videoShareResponseModel!=null){
                                if (videoShareResponseModel.getError_code() == 0){
                                    videoCode = Utility.getVideoCode(videoShareResponseModel.getResource().getLink());
                                    tvTitle.setText(videoShareResponseModel.getResource().getTitle());
                                    tvDescription.setText(videoShareResponseModel.getResource().getDescription());
                                }else{
                                    tvTitle.setText("Invalid link");
                                    tvDescription.setText("");
                                }
                            }else{
                                Utility.showToast(VideoActivity.this,Constants.SOMETHINGWENTWRONG);
                            }
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

        tvDescription.setMovementMethod(BetterLinkMovementMethod.newInstance());
        Linkify.addLinks(tvDescription, Linkify.WEB_URLS);

        BetterLinkMovementMethod.linkify(Linkify.ALL, this)
                .setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
                    @Override
                    public boolean onClick(TextView textView, String url) {
                        Utility.redirectUsingCustomTab(VideoActivity.this,url);
                        return true;
                    }
                });

    }

    private void checkForViews(int resourceId){
        videoViewModel.getResourceById(resourceId, Constants.VIDEO)
                .observeForever( new Observer<ResourceModel>() {
                    @Override
                    public void onChanged(final ResourceModel videoResources) {
                        if (videoResources!=null){
                            Log.d("resource_viewed","beforeliked : "+videoResources.getView()+", "+videoResources.getTotal_views()+", "+videoResources.getId());
                            if (videoResources.getView() == 0){
                                videoViewModel.resourceViewed(prefManager.getString(Constants.JWTTOKEN), videoResources.getId())
                                        .observeForever(new Observer<ResourceViewsResponse>() {
                                            @Override
                                            public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                                if (resourceViewsResponse.getError_code() == 0){
                                                    videoResources.setView(1);
                                                    Log.d("resource_viewed","onliked begore: "+videoResources.getView()+", "+videoResources.getTotal_views()+", "+videoResources.getId());
                                                    videoResources.setTotal_views(videoResources.getTotal_views()+1);
                                                    Log.d("resource_viewed","onliked : "+videoResources.getView()+", "+videoResources.getTotal_views()+", "+videoResources.getId());
                                                    videoViewModel.saveResource(videoResources);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }



    private void setGoogleAdd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        projectAdView.loadAd(adRequest);

        projectAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }


    private void hideShareAndLike() {

        imageViewShare.setEnabled(false);

        imageViewShare.setVisibility(View.INVISIBLE);

    }

    public void showShareAndLike(){
        imageViewShare.setEnabled(true);
        imageViewShare.setVisibility(View.VISIBLE);
    }

    private void share(Uri uri, String text) {
        Utility.shareContent(this,uri,text);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b){

            int mYouTubeFullscreenFlags = YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT |
                    YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI |
                    YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;

            if (!TextUtils.isEmpty(videoCode)){
                mYoutubePlayer = youTubePlayer;
                mYoutubePlayer.loadVideo(videoCode);
                mYoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                mYoutubePlayer.setFullscreenControlFlags(mYouTubeFullscreenFlags);
                mYoutubePlayer.setOnFullscreenListener(this);

            }else if (!TextUtils.isEmpty(playlistId)){
                mYoutubePlayer = youTubePlayer;
                if (videoIndex!=-1){
                    mYoutubePlayer.loadPlaylist(playlistId,videoIndex,0);
                }else {
                    mYoutubePlayer.loadPlaylist(playlistId);
                }
                mYoutubePlayer.setOnFullscreenListener(this);
                mYoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                mYoutubePlayer.setFullscreenControlFlags(mYouTubeFullscreenFlags);
                mYoutubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
                    @Override
                    public void onPrevious() {
                        playlistItemAdapter.onPreviousClicked();
                    }

                    @Override
                    public void onNext() {
                        playlistItemAdapter.onNextClicked();
                    }

                    @Override
                    public void onPlaylistEnded() {

                    }
                });
            }else if (resourceId!=-1 && topicId!=-1){

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
        if (mYoutubePlayer!=null){
            mYoutubePlayer.loadPlaylist(playlistId,videoItemWrapper.getSnippet().getPosition(),0);
        }
    }

    @Override
    public void onRelatedItemClicked(ResourceModel resourceModel, Uri uri) {

        if (resourceModel.getLink().contains("youtu.be") || resourceModel.getLink().contains("youtube")){
            if (mYoutubePlayer!=null){
                String link = resourceModel.getLink();
                String videoCode = Utility.getVideoCode(link);
                mYoutubePlayer.loadVideo(videoCode);
                tvTitle.setText(resourceModel.getTitle());
                tvDescription.setText(resourceModel.getDescription());
                bitmapUri = uri.toString();
                resourceId =resourceModel.getId();

                checkForViews(resourceId);

            }
        }else{
            Utility.redirectUsingCustomTab(this,resourceModel.getLink());
            checkForViews(resourceId);
        }


    }

    @Override
    public void onRelatedItemClicked(LikedResourcesModel resourceModel, Uri uri) {
        if (resourceModel.getLink().contains("youtu.be") || resourceModel.getLink().contains("youtube")){
            if (mYoutubePlayer!=null){
                String link = resourceModel.getLink();
                String videoCode = Utility.getVideoCode(link);
                mYoutubePlayer.loadVideo(videoCode);
                tvTitle.setText(resourceModel.getTitle());
                tvDescription.setText(resourceModel.getDescription());
                bitmapUri = uri.toString();
                resourceId =resourceModel.getId();

                checkForViews(resourceId);
            }
        }else{
            Utility.redirectUsingCustomTab(this,resourceModel.getLink());
            checkForViews(resourceId);
        }
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

    @Override
    public void onBackPressed() {
        if (mYoutubePlayer != null && isFullscreen) {
            mYoutubePlayer.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SEE","Resume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("SEE","Satrt");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SEE","restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SEE","Stop");
    }

    @Override
    public void onFullscreen(boolean b) {
        isFullscreen=b;
        if(b){
            youTubePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
        else{
            final float scale = this.getResources().getDisplayMetrics().density;
            youTubePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,(int) (270 * scale + 0.5f )));
        }
    }
}
