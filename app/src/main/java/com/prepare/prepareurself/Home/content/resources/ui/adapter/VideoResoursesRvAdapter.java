package com.prepare.prepareurself.Home.content.resources.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

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
    public void onBindViewHolder(@NonNull VideoResourcesViewHolder holder, int position) {
        final ResourceModel v1 = resourceModels.get(position);
        final String videoCode = Utility.getVideoCode(v1.getLink());
        holder.bindView(v1, videoCode);
        Log.d("video_debug",videoCode+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.videoClicked(v1,videoCode);
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

        YouTubeThumbnailView imageView;
        TextView tvTitle;
        View bottomView;
        TextView tvDescription;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public VideoResourcesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_video_resources_adapter);
            tvTitle = itemView.findViewById(R.id.tv_video_resources_title);
            tvDescription = itemView.findViewById(R.id.tv_video_resources_decription);
        }

        public void bindView(final ResourceModel v1, final String videoCode) {

            tvTitle.setText(v1.getTitle());
            tvDescription.setText(v1.getDescription());

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
        void videoClicked(ResourceModel videoResources, String videoCode);
    }

}
