package com.prepare.prepareurself.favourites.ui;

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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class LikedResourcesAdapter extends RecyclerView.Adapter<LikedResourcesAdapter.LikedResourceViewHolder> {

    private Context context;
    private List<LikedResourcesModel> likedResourcesModels;
    private LikedResourceRvInteractor listener;

    public LikedResourcesAdapter(Context context,  LikedResourceRvInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setLikedResourcesModels(List<LikedResourcesModel> likedResourcesModels){
        this.likedResourcesModels = likedResourcesModels;
    }

    @NonNull
    @Override
    public LikedResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.liked_resources_rv_adapter_layout,parent,false);
        return new LikedResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LikedResourceViewHolder holder, final int position) {
        final LikedResourcesModel theoryResources1= likedResourcesModels.get(position);
        holder.bindview(theoryResources1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theoryResources1.getType().equalsIgnoreCase("theory")){
                    listener.onResourceClicked(theoryResources1);
                }else{
                    Bitmap bitmap = Utility.getBitmapFromView(holder.youTubeThumbnailView);
                    listener.videoClicked(theoryResources1,Utility.getVideoCode(theoryResources1.getLink()), bitmap);
                }
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (theoryResources1.getType().equalsIgnoreCase("video")){
                    try {
                        String encodedId = Utility.base64EncodeForInt(theoryResources1.getId());
                        Bitmap bitmap = Utility.getBitmapFromView(holder.youTubeThumbnailView);
                        String text = "Checkout our prepareurself app. " +
                                "I found some best resources  from internet at one place and learning is so much fun now.\n" +
                                "You can learn them too here :\n"+
                                "prepareurself.in/resource/"+encodedId;
                        listener.onResourceShared(bitmap,text);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else if (theoryResources1.getType().equalsIgnoreCase("theory")){
                    try {
                        String encodedId = Utility.base64EncodeForInt(theoryResources1.getId());
                        Bitmap bitmap = Utility.getBitmapFromView(holder.imageView);
                        String text = theoryResources1.getTitle()+"\n" +
                                "Prepareurself is providing various courses, projects and resources." +
                                "One place to learn skills and test them by developing projects.\n" +
                                "Checkout prepareurself app : \n" +
                                "prepareurself.in/resource/"+encodedId;
                        listener.onResourceShared(bitmap,text);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (this.likedResourcesModels!=null){
            return this.likedResourcesModels.size();
        }else{
            return 0;
        }
    }

    public class LikedResourceViewHolder extends RecyclerView.ViewHolder{

        private ImageView hitLike;
        private ImageView imageView;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView imgShare;
        private TextView tvViews, tvLikes;
        private YouTubeThumbnailView youTubeThumbnailView;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public LikedResourceViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.topic_image);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
            tvDescription = itemView.findViewById(R.id.tv_decription_topic);
            hitLike=itemView.findViewById(R.id.spark_button);
            imgShare = itemView.findViewById(R.id.img_share_theory_resource);
            tvViews = itemView.findViewById(R.id.no_of_views);
            tvLikes = itemView.findViewById(R.id.tv_no_of_likes);
            youTubeThumbnailView = itemView.findViewById(R.id.topic_image_youtube);

        }

        public  void bindview(final LikedResourcesModel resourceModel){

            if (resourceModel.getType().equalsIgnoreCase("theory")){
                imageView.setVisibility(View.VISIBLE);
                youTubeThumbnailView.setVisibility(View.GONE);
                if (resourceModel.getImage_url()!=null && resourceModel.getImage_url().endsWith(".svg")){
                    Utility.loadSVGImage(context, Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url(), imageView);
                }else{
                    Glide.with(context).load(
                            Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url())
                            .placeholder(R.drawable.placeholder)
                            .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                            .into(imageView);
                }
                Log.d("liked", "bindview: "+Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url());
            }else if (resourceModel.getType().equalsIgnoreCase("video")){

                imageView.setVisibility(View.GONE);
                youTubeThumbnailView.setVisibility(View.VISIBLE);

                final String videoCode = Utility.getVideoCode(resourceModel.getLink());

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

            tvTitle.setText(resourceModel.getTitle());
            tvDescription.setText(resourceModel.getDescription());
            tvViews.setText(resourceModel.getTotal_views()+" views");
            tvLikes.setText(resourceModel.getTotal_likes() + " likes");

        }

    }

    public interface LikedResourceRvInteractor{
        void onResourceClicked(LikedResourcesModel resource);
        void OnLikeButtonClicked(LikedResourcesModel resource,int checked);
        void onResourceShared(Bitmap bitmap, String text);
        void videoClicked(LikedResourcesModel videoResources, String videoCode, Bitmap bitmap);
    }

}
