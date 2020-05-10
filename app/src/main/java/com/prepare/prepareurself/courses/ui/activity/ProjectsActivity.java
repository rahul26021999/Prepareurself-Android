package com.prepare.prepareurself.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.courses.data.model.ProjectResponseModel;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.ui.adapters.PlaylistVideosRvAdapter;
import com.prepare.prepareurself.courses.viewmodels.ProjectsViewModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.utils.BaseActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.SingleVIdeoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubePlaylistResponseModel;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class ProjectsActivity extends BaseActivity implements PlaylistVideosRvAdapter.PlaylistVideoRvInteractor , View.OnClickListener {

    int projectId = -1;
    private ProjectsViewModel viewModel;
    private TextView tvProjectTitle, tvProjectDescription;
    private ImageView imageProject,backBtn;
    private RecyclerView recyclerView;
    private PlaylistVideosRvAdapter adapter;
    private String rvNextPageToken = "";
    String playlist="";
    private TextView tvLoading,title;
    private CardView cardImageView;
    private ImageView videoImageView;
    private TextView tvCardVideoTitle, tvReferenceHeader, tvViewPlaylist;
    private String videoTitle = "", videoDescription="", videoCode ="";
    private String projectTitle = "";
    private String projectDescription = "";
    private PrefManager prefManager;
    private ProgressBar progressBar;
    private String courseName;
    private RelativeLayout singleCardRel;
    private ImageView shareImageView;
    private ImageView likeButton;

    private TextView level;
    private AdView mAdView;
    ProjectsModel currentProjectModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        viewModel = ViewModelProviders.of(this).get(ProjectsViewModel.class);

        tvProjectTitle = findViewById(R.id.tv_project_title);
        tvProjectDescription = findViewById(R.id.tv_project_decription);
        imageProject = findViewById(R.id.image_project);
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.rv_prjects_videos);
        tvLoading = findViewById(R.id.tv_loading_project);
        cardImageView = findViewById(R.id.card_single);
        videoImageView = findViewById(R.id.project_video_image_view);
        tvCardVideoTitle = findViewById(R.id.tv_card_video_project);
        tvReferenceHeader = findViewById(R.id.tv_reference_heading);
        tvViewPlaylist = findViewById(R.id.tv_viewfullplaylist);
        progressBar = findViewById(R.id.loading_project);
        level=findViewById(R.id.level);
        mAdView=findViewById(R.id.adView);
        title=findViewById(R.id.title);
        singleCardRel = findViewById(R.id.singleContainerCard);
        shareImageView = findViewById(R.id.image_project_share);
        likeButton = findViewById(R.id.spark_button);

        recyclerView.setVisibility(View.GONE);
        cardImageView.setVisibility(View.GONE);
        tvViewPlaylist.setVisibility(View.GONE);
        tvLoading.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        backBtn.setOnClickListener(this);
        prefManager = new PrefManager(this);

        title.setText("Project");

        setGoogleAdd();

        Intent intent = getIntent();

        if (intent.getData()!=null){

            Log.d("project_share", intent.getData().toString());
            String[] tempData = intent.getData().toString().split("&");
            int id = Integer.parseInt(tempData[1].split("id=")[1]);
            courseName = tempData[2].split("courseName=")[1];

            if (!prefManager.getBoolean(Constants.ISLOGGEDIN)){
                Intent loginIntent = new Intent(ProjectsActivity.this, AuthenticationActivity.class);
                loginIntent.putExtra(Constants.PROJECTID, id);
                loginIntent.putExtra(Constants.COURSENAME, courseName);
                startActivity(loginIntent);
                finish();
            }else {
                projectId = id;
            }

        }else if (intent.getBooleanExtra(Constants.DEEPSHAREPROECTAFTERLOGIN, false)){
            projectId = intent.getIntExtra(Constants.PROJECTID,-1);
            courseName = intent.getStringExtra(Constants.COURSENAME);
        }else{
            projectId = intent.getIntExtra(Constants.PROJECTID,-1);
        }



        if (projectId != -1){

            viewModel.getProjectByIdFromRemote(prefManager.getString(Constants.JWTTOKEN), projectId);


            viewModel.getProjectById(projectId).observe(this, new Observer<ProjectsModel>() {
                @Override
                public void onChanged(ProjectsModel projectsModel) {
                    if (projectsModel!=null){
                        if (projectsModel.getLink().contains("youtu.be") || projectsModel.getLink().contains("youtube")){
                            updateUIWithProject(projectsModel);
                        }else{
                            if (projectsModel.getView() == 0){
                                viewModel.viewProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId())
                                        .observe(ProjectsActivity.this, new Observer<ResourceViewsResponse>() {
                                            @Override
                                            public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                                                if (resourceViewsResponse!=null && resourceViewsResponse.getError_code() == 0){
                                                       Log.d("project_viewed", "theory project view : done "+ resourceViewsResponse.getMessage() );
                                                }
                                            }
                                        });
                            }
                            Utility.redirectUsingCustomTab(ProjectsActivity.this,projectsModel.getLink());
                            finish();
                        }

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

        tvProjectDescription.setMovementMethod(BetterLinkMovementMethod.newInstance());
        Linkify.addLinks(tvProjectDescription, Linkify.WEB_URLS);

        BetterLinkMovementMethod.linkify(Linkify.ALL, this)
                .setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
                    @Override
                    public boolean onClick(TextView textView, String url) {
                        Utility.redirectUsingCustomTab(ProjectsActivity.this,url);
                        return true;
                    }
                });


    }

    private void setGoogleAdd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

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

            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    private void updateUIWithProject(final ProjectsModel projectsModel) {

        if (projectsModel.getImage_url()!=null && projectsModel.getImage_url().endsWith(".svg")){
            Utility.loadSVGImage(ProjectsActivity.this, Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url(), imageProject);
        }else{
            Glide.with(this)
                    .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                    .placeholder(R.drawable.placeholder)
                    .into(imageProject);
        }

        if (projectsModel.getLike() == 1){
            likeButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
        }else if (projectsModel.getLike() == 0){
            likeButton.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
        }

        if (projectsModel.getView() == 0){
            viewModel.viewProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId())
                    .observe(this, new Observer<ResourceViewsResponse>() {
                        @Override
                        public void onChanged(ResourceViewsResponse resourceViewsResponse) {
                            if (resourceViewsResponse!=null && resourceViewsResponse.getError_code() == 0){
                                Log.d("project_viewed", "video project view : done "+ resourceViewsResponse.getMessage() );
                            }
                        }
                    });
        }

        if(projectsModel.getLevel().equals("hard"))
            level.setTextColor(getResources().getColorStateList(R.color.lightred));
        else if(projectsModel.getLevel().equals("easy"))
            level.setTextColor(getResources().getColorStateList(R.color.lightgreen));
        else
            level.setTextColor(getResources().getColorStateList(R.color.yellow));

        level.setText(projectsModel.getLevel());
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

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Utility.getUriOfBitmap(Utility.getBitmapFromView(imageProject),ProjectsActivity.this);
                    String encodedId = Utility.base64EncodeForInt(projectsModel.getId());
                    String text = projectsModel.getName()+"\n\n" +
                            "Prepareurself is providing various courses, projects and resources." +
                            "One place to learn skills and test them by developing projects.\n" +
                            "Checkout prepareurself app : \n" +
                            "prepareurself.in/project/"+encodedId;
                    Utility.shareContent(ProjectsActivity.this,uri,text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {

            ValueAnimator buttonColorAnim = null;

            @Override
            public void onClick(View v) {

                if (projectsModel.getLike() == 1){
                    onProjectLiked(projectsModel,1);
                }else if (projectsModel.getLike() == 0){
                    onProjectLiked(projectsModel,0);
                }

                /*if (buttonColorAnim == null && projectsModel.getLike() ==1){
                    likeButton.setImageDrawable(ProjectsActivity.this.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
                    onProjectLiked(projectsModel,1);
                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()-1);
                    projectsModel.setLike(0);
                }else{
                    if(buttonColorAnim != null){
                        buttonColorAnim.reverse();
                        buttonColorAnim = null;
                        onProjectLiked(projectsModel,1);
                    }
                    else {
                        final ImageView button = (ImageView) v;
                        buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), ProjectsActivity.this.getResources().getColor(R.color.like_grey), ProjectsActivity.this.getResources().getColor(R.color.like_blue));
                        buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                button.setColorFilter((Integer) animator.getAnimatedValue());
                            }
                        });
                        buttonColorAnim.start();
                        onProjectLiked(projectsModel,0);
                    }
                }*/

            }
        });


    }

    private void onProjectLiked(final ProjectsModel projectsModel, final int i) {
        viewModel.likeProject(prefManager.getString(Constants.JWTTOKEN),projectsModel.getId(), i)
                .observe(this, new Observer<ResourceLikesResponse>() {
                    @Override
                    public void onChanged(ResourceLikesResponse resourceLikesResponse) {
                        if (resourceLikesResponse!=null){
                            if (!resourceLikesResponse.getSuccess()){
                                Utility.showToast(ProjectsActivity.this,"Unable to like at the moment");
                            }else{

                                if (i==0){
                                    projectsModel.setLike(1);
                                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()+1);
                                }else{
                                    projectsModel.setLike(0);
                                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()-1);
                                }
                                viewModel.saveProject(projectsModel);
                            }
                        }
                    }
                });
    }

    private void loadSingleLink(ProjectsModel projectsModel) {

        tvReferenceHeader.setText("Video");
        recyclerView.setVisibility(View.GONE);
        tvViewPlaylist.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        cardImageView.setVisibility(View.VISIBLE);
        final String videoCode = Utility.getVideoCode(projectsModel.getLink());
        loadCardImageViewWithVideo(videoCode);
        cardImageView.setOnClickListener(this);
        tvCardVideoTitle.setOnClickListener(this);
    }

    private void loadCardImageViewWithVideo(final String v) {

        viewModel.fetchVideoDetails(v).observe(this, new Observer<SingleVIdeoItemWrapper>() {
            @Override
            public void onChanged(final SingleVIdeoItemWrapper singleVIdeoItemWrapper) {

                if (singleVIdeoItemWrapper!=null){
                    tvLoading.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    tvViewPlaylist.setVisibility(View.GONE);
                    videoCode = v;
                    tvCardVideoTitle.setText(singleVIdeoItemWrapper.getSnippet().getTitle());
                    videoTitle = singleVIdeoItemWrapper.getSnippet().getTitle();
                    videoDescription = singleVIdeoItemWrapper.getSnippet().getDescription();

                    if(singleVIdeoItemWrapper.getSnippet().getThumbnails().getMaxres()!=null){
                        Glide.with(ProjectsActivity.this)
                                .load(singleVIdeoItemWrapper.getSnippet().getThumbnails().getMaxres().getUrl())
                                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_image_loading_error)
                                .into(videoImageView);
                    }else if(singleVIdeoItemWrapper.getSnippet().getThumbnails().getHigh()!=null){
                        Glide.with(ProjectsActivity.this)
                                .load(singleVIdeoItemWrapper.getSnippet().getThumbnails().getHigh().getUrl())
                                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_image_loading_error)
                                .into(videoImageView);
                    }else if (singleVIdeoItemWrapper.getSnippet().getThumbnails().getMedium()!=null){
                        Glide.with(ProjectsActivity.this)
                                .load(singleVIdeoItemWrapper.getSnippet().getThumbnails().getMedium().getUrl())
                                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_image_loading_error)
                                .into(videoImageView);
                    }else if (singleVIdeoItemWrapper.getSnippet().getThumbnails().getDefaultThumbnail()!=null){
                        Glide.with(ProjectsActivity.this)
                                .load(singleVIdeoItemWrapper.getSnippet().getThumbnails().getDefaultThumbnail().getUrl())
                                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_image_loading_error)
                                .into(videoImageView);
                    }else if (singleVIdeoItemWrapper.getSnippet().getThumbnails().getStandard()!=null){
                        Glide.with(ProjectsActivity.this)
                                .load(singleVIdeoItemWrapper.getSnippet().getThumbnails().getStandard().getUrl())
                                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.ic_image_loading_error)
                                .into(videoImageView);
                    }

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
                                progressBar.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.card_single:
            case R.id.tv_card_video_project:
                if (!TextUtils.isEmpty(videoTitle)){
                    Intent intent = new Intent(ProjectsActivity.this,VideoActivity.class);
                    intent.putExtra(Constants.VIDEOCODE,videoCode);
                    intent.putExtra(Constants.VIDEOTITLE, videoTitle);
                    intent.putExtra(Constants.VIDEODESCRIPTION, videoDescription);
                    intent.putExtra(Constants.SINGLEVIDEO,true);
                    startActivity(intent);
                }
                break;
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}


