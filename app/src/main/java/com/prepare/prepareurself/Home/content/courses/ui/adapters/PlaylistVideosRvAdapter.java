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
    private PlaylistVideoRvInteractor listener;

    public PlaylistVideosRvAdapter(Context context, PlaylistVideoRvInteractor listener) {
        this.context = context;
        this.listener = listener;
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
        final VideoItemWrapper v = videoContentDetails.get(position);
        holder.bindView(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVideoClicked(v, v.getContentDetails().getVideoId());
            }
        });
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
        }

    }

    public interface PlaylistVideoRvInteractor{
        void onVideoClicked(VideoItemWrapper videoItemWrapper, String videoCode);
    }

}
