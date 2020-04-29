package com.prepare.prepareurself.resources.ui.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;
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
                Bitmap bitmap = Utility.getBitmapFromView(holder.imageView);
                listener.videoClicked(v1,videoCode, bitmap);
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String encodedId = Utility.base64EncodeForInt(v1.getId());
                    Bitmap bitmap = Utility.getBitmapFromView(holder.imageView);
                    String text = "Checkout our prepareurself app. " +
                            "I found some best resources  from internet at one place and learning is so much fun now.\n" +
                            "You can learn them too here :\n"+
                            "prepareurself.in/video/"+encodedId;
                    listener.onResourceShared(bitmap,text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.hitLike.setOnClickListener(new View.OnClickListener() {

            ValueAnimator buttonColorAnim = null;


            @Override
            public void onClick(View v) {

                if (buttonColorAnim == null && v1.getLike() ==1){
                    holder.hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
                    listener.onVideoResourceLiked(v1,1);
                    holder.tvLikes.setText(v1.getTotal_likes()-1 + " likes");
                    v1.setTotal_likes(v1.getTotal_likes()-1);
                    v1.setLike(0);
                }else{
                    if(buttonColorAnim != null){
                        buttonColorAnim.reverse();
                        buttonColorAnim = null;
                        listener.onVideoResourceLiked(v1,1);
                        holder.tvLikes.setText(v1.getTotal_likes() + " likes");
                      }
                    else {
                        final ImageView button = (ImageView) v;
                        buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), context.getResources().getColor(R.color.like_grey), context.getResources().getColor(R.color.like_blue));
                        buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                button.setColorFilter((Integer) animator.getAnimatedValue());
                            }
                        });
                        buttonColorAnim.start();
                        listener.onVideoResourceLiked(v1,0);
                        holder.tvLikes.setText(v1.getTotal_likes()+1 + " likes");
                    }
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
        private YouTubeThumbnailView imageView;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView imgShare;
        private TextView tvViews, tvLikes;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public VideoResourcesViewHolder(@NonNull View itemView) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.img_video_resources_adapter);
//            tvTitle = itemView.findViewById(R.id.tv_video_resources_title);
//            tvDescription = itemView.findViewById(R.id.tv_video_resources_decription);

            imageView = itemView.findViewById(R.id.topic_image);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
            tvDescription = itemView.findViewById(R.id.tv_decription_topic);
            hitLike=itemView.findViewById(R.id.spark_button);
            imgShare = itemView.findViewById(R.id.img_share_theory_resource);
            tvViews = itemView.findViewById(R.id.no_of_views);
            tvLikes = itemView.findViewById(R.id.tv_no_of_likes);
        }

        public void bindView(final ResourceModel v1, final String videoCode) {

            tvTitle.setText(v1.getTitle());
            tvDescription.setText(v1.getDescription());
            tvLikes.setText(v1.getTotal_likes() + " likes");
            tvViews.setText(v1.getTotal_views() + " views");

            if (v1.getLike() == 1){
                hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
            }else{
                hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
            }

            if (readyForLoadingYoutubeThumbnail){
                readyForLoadingYoutubeThumbnail = false;

                imageView.initialize(Constants.YOUTUBE_PLAYER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
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
                            }
                        });

                        readyForLoadingYoutubeThumbnail = true;

                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        String errorMessage = String.format(
                                context.getString(R.string.error_player), youTubeInitializationResult.toString());
                        Utility.showToast(context,errorMessage);

                        readyForLoadingYoutubeThumbnail = true;
                    }
                });

            }

        }
    }

    public interface VideoResourceInteractor{
        void videoClicked(ResourceModel videoResources, String videoCode, Bitmap bitmap);
        void onVideoResourceLiked(ResourceModel resourceModel, int liked);
        void onResourceShared(Bitmap bitmap, String text);
    }

}
