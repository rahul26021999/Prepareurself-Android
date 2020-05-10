package com.prepare.prepareurself.dashboard.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResourceRvHorizontalAdapter extends RecyclerView.Adapter<ResourceRvHorizontalAdapter.ResourceViewHolder> {

    private Context context;
    private List<ResourceModel> resourceModels;
    private ResourceHomePageListener listener;
    private boolean isViews = false;
    private boolean isLikes = false;
    private boolean isPostedOn = false;

    public ResourceRvHorizontalAdapter(Context context, ResourceHomePageListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResourceModels(List<ResourceModel> resourceModels){
        this.resourceModels = resourceModels;
    }

    public void setParams(boolean isViews, boolean isPostedOn, boolean isLikes){
        this.isViews = isViews;
        this.isPostedOn = isPostedOn;
        this.isLikes = isLikes;
    }

    public interface ResourceHomePageListener{
        void onResourceHomeClicked(ResourceModel resourceModel);
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resource_viewtype_rv_layout,parent, false);

        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, final int position) {
        holder.bindView(context, resourceModels.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResourceHomeClicked(resourceModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (resourceModels!=null){
            return resourceModels.size();
        }else {
            return 0;
        }
    }

    public class ResourceViewHolder extends RecyclerView.ViewHolder{

        TextView name, views, likes, posteon;
        ImageView imageView;
        YouTubeThumbnailView youTubeThumbnailView;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image_course_viewtype);
            youTubeThumbnailView = itemView.findViewById(R.id.image_thumb_course_viewtype);
            views=itemView.findViewById(R.id.views);
            likes=  itemView.findViewById(R.id.likes);
            posteon =itemView.findViewById(R.id.postedon);
            youTubeThumbnailView.setVisibility(View.GONE);
        }

        public void bindView(final Context context, ResourceModel res){

            name.setText(res.getTitle());
            if (isViews){
                views.setVisibility(View.VISIBLE);
                views.setText(res.getTotal_views()+ " views");
            }
            else{
                views.setVisibility(View.GONE);
            }
            if (isLikes){
                likes.setVisibility(View.VISIBLE);
                likes.setText(res.getTotal_likes() + " likes");
            }else{
                likes.setVisibility(View.GONE);
            }

            if (isPostedOn){
                posteon.setVisibility(View.VISIBLE);
                String duration = Utility.getDurationBetweenTwoDays(res.getCreated_at().split(" ")[0]);
                if (!TextUtils.isEmpty(duration)){
                    posteon.setText(duration);
                }else{
                    posteon.setVisibility(View.GONE);
                }
            }else{
                posteon.setVisibility(View.GONE);
            }

            if (res.getType().equalsIgnoreCase("theory")){
                youTubeThumbnailView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                if (res.getImage_url()!=null && res.getImage_url().endsWith(".svg")){
                    Utility.loadSVGImage(context, Constants.THEORYRESOURCEBASEURL+ res.getImage_url(), imageView);
                }else{

                    Picasso.get()
                            .load(Constants.THEORYRESOURCEBASEURL+ res.getImage_url())
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);

//                    Glide.with(context).load(
//                            Constants.THEORYRESOURCEBASEURL+ res.getImage_url())
//                            .placeholder(R.drawable.placeholder)
//                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                            .centerInside()
//                            .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
//                            .into(imageView);
                }
            }else if (res.getType().equalsIgnoreCase("video")){

                if (res.getLink().contains("youtu.be") || res.getLink().contains("youtube")){

                    youTubeThumbnailView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);

                    final String videoCode = Utility.getVideoCode(res.getLink());

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

                }else{

                    youTubeThumbnailView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    if (res.getImage_url()!=null && res.getImage_url().endsWith(".svg")){
                        Utility.loadSVGImage(context, Constants.THEORYRESOURCEBASEURL+ res.getImage_url(), imageView);
                    }else{

                        Picasso.get()
                                .load(Constants.THEORYRESOURCEBASEURL+ res.getImage_url())
                                .placeholder(R.drawable.placeholder)
                                .into(imageView);

//                        Glide.with(context).load(
//                                Constants.THEORYRESOURCEBASEURL+ res.getImage_url())
//                                .placeholder(R.drawable.placeholder)
//                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                                .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
//                                .into(imageView);
                    }

                }
            }





        }

    }
}
