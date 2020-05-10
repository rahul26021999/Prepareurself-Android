package com.prepare.prepareurself.resources.ui.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class VideoResoursesRvAdapter extends RecyclerView.Adapter<VideoResoursesRvAdapter.VideoResourcesViewHolder> {

    Context context;
    List<ResourceModel> resourceModels;
    private VideoResourceInteractor listener;

    public VideoResoursesRvAdapter(Context context, VideoResourceInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResourceModels(List<ResourceModel> resourceModels) {
        this.resourceModels = resourceModels;
    }

    @NonNull
    @Override
    public VideoResourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_resources_rv_adapter_layout,parent,false);

        return new VideoResourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoResourcesViewHolder holder, int position) {
        final ResourceModel v1 = resourceModels.get(position);
        final String videoCode = Utility.getVideoCode(v1.getLink());
        holder.bindView(v1, videoCode);
        Log.d("video_debug",videoCode+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v1.getLink().contains("youtu.be") || v1.getLink().contains("youtube")){
                    Bitmap bitmap = Utility.getBitmapFromView(holder.youTubeThumbnailView);
                    listener.videoClicked(v1,videoCode, bitmap);
                }else{
                    listener.videoClicked(v1,videoCode, null);
                }


            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String encodedId = Utility.base64EncodeForInt(v1.getId());
                    Bitmap bitmap = Utility.getBitmapFromView(holder.youTubeThumbnailView);
                    String text = "Checkout our prepareurself app. " +
                            "I found some best resources  from internet at one place and learning is so much fun now.\n" +
                            "You can learn them too here :\n"+
                            "prepareurself.in/resource/"+encodedId;
                    listener.onResourceShared(bitmap,text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.hitLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v1.getLike() == 0){
                    listener.onVideoResourceLiked(v1, 0);
                }else if (v1.getLike() == 1){
                    listener.onVideoResourceLiked(v1, 1);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (resourceModels!=null)
            return resourceModels.size();
        else
            return 0;
    }

    class VideoResourcesViewHolder extends RecyclerView.ViewHolder{

//        YouTubeThumbnailView imageView;
//        TextView tvTitle;
//        View bottomView;
//        TextView tvDescription;


        private ImageView hitLike;
        private YouTubeThumbnailView youTubeThumbnailView;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView imgShare, imageView;
        private TextView tvViews, tvLikes;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public VideoResourcesViewHolder(@NonNull View itemView) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.img_video_resources_adapter);
//            tvTitle = itemView.findViewById(R.id.tv_video_resources_title);
//            tvDescription = itemView.findViewById(R.id.tv_video_resources_decription);

            youTubeThumbnailView = itemView.findViewById(R.id.topic_image_youtube);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
            tvDescription = itemView.findViewById(R.id.tv_decription_topic);
            hitLike=itemView.findViewById(R.id.spark_button);
            imgShare = itemView.findViewById(R.id.img_share_theory_resource);
            tvViews = itemView.findViewById(R.id.no_of_views);
            tvLikes = itemView.findViewById(R.id.tv_no_of_likes);
            imageView = itemView.findViewById(R.id.topic_image);
        }

        public void bindView(final ResourceModel v1, final String videoCode) {

            tvTitle.setText(v1.getTitle());
            tvDescription.setText(v1.getDescription());
            tvLikes.setText(v1.getTotal_likes() + " likes");
            tvViews.setText(v1.getTotal_views() + " views");

            if (v1.getLink().contains("youtu.be") || v1.getLink().contains("youtube")){
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                if (v1.getLike() == 1){
                    hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
                }else{
                    hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
                }

                if (readyForLoadingYoutubeThumbnail){
                    readyForLoadingYoutubeThumbnail = false;

                    youTubeThumbnailView.initialize(Constants.YOUTUBE_PLAYER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            youTubeThumbnailLoader.setVideo(videoCode);

                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                                @Override
                                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                    youTubeThumbnailLoader.release();
                                }

                                @Override
                                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                                    Utility.showToast(context,Constants.UNABLETOLOADVIDEOSATTHEMOMENT);
                                    Log.d("video_resource", "onThumbnailError: "+v1.getTitle());
                                }
                            });

                            readyForLoadingYoutubeThumbnail = true;

                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                            String errorMessage = String.format(
                                    context.getString(R.string.error_player), youTubeInitializationResult.toString());
                            Utility.showToast(context,errorMessage);
                            Log.d("video_resource", "onInitializeError: "+v1.getTitle());

                            readyForLoadingYoutubeThumbnail = true;
                        }
                    });

                }

            }else{
                youTubeThumbnailView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                if (v1.getImage_url()!=null && v1.getImage_url().endsWith(".svg")){
                    Utility.loadSVGImage(context,Constants.THEORYRESOURCEBASEURL + v1.getImage_url(), imageView);
                }else{
                    Glide.with(context).load(
                            Constants.THEORYRESOURCEBASEURL + v1.getImage_url())
                            .placeholder(R.drawable.placeholder)
                            .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                            .into(imageView);
                }

            }

        }
    }

    public interface VideoResourceInteractor{
        void videoClicked(ResourceModel videoResources, String videoCode, Bitmap bitmap);
        void onVideoResourceLiked(ResourceModel resourceModel, int liked);
        void onResourceShared(Bitmap bitmap, String text);
    }

}
