package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui;

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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class LikedRelatedVideosRvAdapter extends RecyclerView.Adapter<LikedRelatedVideosRvAdapter.RelatedVideosViewHolder> {

    private Context context;
    private List<LikedResourcesModel> resourceModels;
    private RelatedRvListener listener;

    public LikedRelatedVideosRvAdapter(Context context, RelatedRvListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResources(List<LikedResourcesModel> resources){
        this.resourceModels = resources;
    }

    @NonNull
    @Override
    public RelatedVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.related_videos_rv_adapter_layout,parent, false);
        return new RelatedVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedVideosViewHolder holder, int position) {
        final LikedResourcesModel resourceModel = resourceModels.get(position);
        holder.bindView(resourceModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRelatedItemClicked(resourceModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (resourceModels!=null){
            return resourceModels.size();
        }else{
            return 0;
        }
    }

    public class RelatedVideosViewHolder extends RecyclerView.ViewHolder{

        YouTubeThumbnailView youTubeThumbnailView;
        TextView textView;
        ImageView imageView;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public RelatedVideosViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubeThumbnailView = itemView.findViewById(R.id.related_video_youtubethumnail);
            textView = itemView.findViewById(R.id.tv_rekated_title);
            imageView = itemView.findViewById(R.id.related_video_imageview);
        }

        public void bindView(LikedResourcesModel resourceModel) {

            textView.setText(resourceModel.getTitle());

            if (resourceModel.getLink().contains("youtu.be") || resourceModel.getLink().contains("youtube")){

                youTubeThumbnailView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

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
            }else{

                youTubeThumbnailView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                if (resourceModel.getImage_url()!=null && resourceModel.getImage_url().endsWith(".svg")){
                    Utility.loadSVGImage(context,Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url(), imageView);
                }else{
                    Glide.with(context).load(
                            Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url())
                            .placeholder(R.drawable.placeholder)
                            .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                            .into(imageView);
                }
            }

        }
    }

    public interface RelatedRvListener{
        void onRelatedItemClicked(LikedResourcesModel resourceModel);
    }

}
