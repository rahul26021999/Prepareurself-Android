package com.example.prepareurself.Home.content.resources.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prepareurself.Home.content.resources.model.VideoResources;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.Utility;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;


public class VideoResoursesRvAdapter extends RecyclerView.Adapter<VideoResoursesRvAdapter.VideoResourcesViewHolder> {

    Context context;
    List<VideoResources> videoResources;
    private VideoResourceInteractor listener;

    public VideoResoursesRvAdapter(Context context, VideoResourceInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setVideoResources(List<VideoResources> videoResources) {
        this.videoResources = videoResources;
    }

    @NonNull
    @Override
    public VideoResourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_resources_rv_adapter_layout,parent,false);

        return new VideoResourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoResourcesViewHolder holder, int position) {
        final VideoResources v1 = videoResources.get(position);
        holder.bindView(v1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.videoClicked(v1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoResources.size();
    }

    class VideoResourcesViewHolder extends RecyclerView.ViewHolder{

        YouTubeThumbnailView imageView;
        TextView tvTitle;
        View bottomView;

        public VideoResourcesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_video_resources_adapter);
            tvTitle = itemView.findViewById(R.id.tv_video_resources_title);
        }

        public void bindView(final VideoResources v1) {

            imageView.initialize(Constants.YOUTUBE_PLAYER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(v1.getVideoCode());

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

                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    String errorMessage = String.format(
                            context.getString(R.string.error_player), youTubeInitializationResult.toString());
                    Utility.showToast(context,errorMessage);
                }
            });
            tvTitle.setText(v1.getVideoTitle());
        }
    }

    public interface VideoResourceInteractor{
        void videoClicked(VideoResources videoResources);
    }

}
