package com.prepare.prepareurself.Home.content.courses.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;

import java.util.List;

public class PlaylistVideosRvAdapter extends RecyclerView.Adapter<PlaylistVideosRvAdapter.PlaylistVideosViewHolder> {

    private List<VideoContentDetails> videoContentDetails;
    private Context context;

    public PlaylistVideosRvAdapter(Context context) {
        this.context = context;
    }

    public void setVideoContentDetails(List<VideoContentDetails> videoContentDetails){
        this.videoContentDetails = videoContentDetails;
    }

    @NonNull
    @Override
    public PlaylistVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_videos_item_layout, parent, false);

        return new PlaylistVideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistVideosViewHolder holder, int position) {
        VideoContentDetails v = videoContentDetails.get(position);
        holder.bindView(v);
    }

    @Override
    public int getItemCount() {
        if (videoContentDetails!=null){
            return videoContentDetails.size();
        }else{
            return 0;
        }
    }

    public class PlaylistVideosViewHolder extends RecyclerView.ViewHolder{

        YouTubeThumbnailView youTubeThumbnailView;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public PlaylistVideosViewHolder(@NonNull View itemView) {
            super(itemView);

            youTubeThumbnailView = itemView.findViewById(R.id.playlist_videos_thumnail);

        }

        public void bindView(final VideoContentDetails contentDetails){

            if (readyForLoadingYoutubeThumbnail){
                readyForLoadingYoutubeThumbnail = false;
                youTubeThumbnailView.initialize(Constants.YOUTUBE_PLAYER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(contentDetails.getVideoId());

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
