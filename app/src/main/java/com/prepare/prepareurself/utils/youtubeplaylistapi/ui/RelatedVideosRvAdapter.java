package com.prepare.prepareurself.utils.youtubeplaylistapi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class RelatedVideosRvAdapter extends RecyclerView.Adapter<RelatedVideosRvAdapter.RelatedVideosViewHolder> {

    private Context context;
    private List<ResourceModel> resourceModels;
    private RelatedRvListener listener;

    public RelatedVideosRvAdapter(Context context, RelatedRvListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResources(List<ResourceModel> resources){
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
        final ResourceModel resourceModel = resourceModels.get(position);
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

        private boolean readyForLoadingYoutubeThumbnail = true;

        public RelatedVideosViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubeThumbnailView = itemView.findViewById(R.id.related_video_youtubethumnail);
            textView = itemView.findViewById(R.id.tv_rekated_title);
        }

        public void bindView(ResourceModel resourceModel) {

            textView.setText(resourceModel.getTitle());

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
    }

    public interface RelatedRvListener{
        void onRelatedItemClicked(ResourceModel resourceModel);
    }

}
