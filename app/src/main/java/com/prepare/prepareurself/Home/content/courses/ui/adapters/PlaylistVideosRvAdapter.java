package com.prepare.prepareurself.Home.content.courses.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

public class PlaylistVideosRvAdapter extends RecyclerView.Adapter<PlaylistVideosRvAdapter.PlaylistVideosViewHolder> {

    private List<VideoItemWrapper> videoContentDetails;
    private Context context;

    public PlaylistVideosRvAdapter(Context context) {
        this.context = context;
    }

    public void setVideoContentDetails(List<VideoItemWrapper> videoContentDetails){
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
        VideoItemWrapper v = videoContentDetails.get(position);
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

        ImageView youTubeThumbnailView;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public PlaylistVideosViewHolder(@NonNull View itemView) {
            super(itemView);

            youTubeThumbnailView = itemView.findViewById(R.id.playlist_videos_thumnail);

        }

        public void bindView(final VideoItemWrapper contentDetails){

            Glide.with(context)
                    .load(contentDetails.getSnippet().getThumbnails().getMedium().getUrl())
                    .override(180,150)
                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_image_loading_error)
                    .into(youTubeThumbnailView);



//            if (readyForLoadingYoutubeThumbnail){
//                readyForLoadingYoutubeThumbnail = false;
//                youTubeThumbnailView.initialize(Constants.YOUTUBE_PLAYER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//                    @Override
//                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                        youTubeThumbnailLoader.setVideo(contentDetails.getVideoId());
//
//                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                            @Override
//                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                                youTubeThumbnailLoader.release();
//                            }
//
//                            @Override
//                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                                Utility.showToast(context,Constants.UNABLETOLOADVIDEOSATTHEMOMENT);
//                            }
//                        });
//
//                        readyForLoadingYoutubeThumbnail = true;
//
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                        String errorMessage = String.format(
//                                context.getString(R.string.error_player), youTubeInitializationResult.toString());
//                        Utility.showToast(context,errorMessage);
//
//                        readyForLoadingYoutubeThumbnail = true;
//                    }
//                });
//            }

        }

    }

}
