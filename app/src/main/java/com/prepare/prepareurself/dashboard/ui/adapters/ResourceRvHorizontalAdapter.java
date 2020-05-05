package com.prepare.prepareurself.dashboard.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.List;

public class ResourceRvHorizontalAdapter extends RecyclerView.Adapter<ResourceRvHorizontalAdapter.ResourceViewHolder> {

    private Context context;
    private List<ResourceModel> resourceModels;
    private ResourceHomePageListener listener;

    public ResourceRvHorizontalAdapter(Context context, ResourceHomePageListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResourceModels(List<ResourceModel> resourceModels){
        this.resourceModels = resourceModels;
    }

    public interface ResourceHomePageListener{
        void onResourceHomeClicked(ResourceModel resourceModel);
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_viewtype_rv_layout,parent, false);

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

        TextView tvCourseName;
        ImageView imageView;
        YouTubeThumbnailView youTubeThumbnailView;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tv_course_name_viewtype);
            imageView = itemView.findViewById(R.id.image_course_viewtype);
            youTubeThumbnailView = itemView.findViewById(R.id.image_thumb_course_viewtype);
            youTubeThumbnailView.setVisibility(View.GONE);
        }

        public void bindView(final Context context, ResourceModel res){

            tvCourseName.setText(res.getTitle());

            if (res.getType().equalsIgnoreCase("theory")){
                if (res.getImage_url()!=null && res.getImage_url().endsWith(".svg")){
                    Utility.loadSVGImage(context, Constants.THEORYRESOURCEBASEURL+ res.getImage_url(), imageView);
                }else{
                    Glide.with(context).load(
                            Constants.THEORYRESOURCEBASEURL+ res.getImage_url())
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override(500,500)
                            .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                            .into(imageView);
                }
            }else if (res.getType().equalsIgnoreCase("video")){

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

            }





        }

    }
}
