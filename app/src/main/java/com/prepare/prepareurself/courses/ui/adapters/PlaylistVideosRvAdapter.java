package com.prepare.prepareurself.courses.ui.adapters;

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
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;

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
        TextView tvTitle;

        private boolean readyForLoadingYoutubeThumbnail = true;

        public PlaylistVideosViewHolder(@NonNull View itemView) {
            super(itemView);

            youTubeThumbnailView = itemView.findViewById(R.id.playlist_videos_thumnail);
            tvTitle = itemView.findViewById(R.id.tv_playlist_video_title);

        }

        public void bindView(final VideoItemWrapper contentDetails){

            String imageUrl = contentDetails.getSnippet().getThumbnails().getHigh().getUrl();

            if (imageUrl!=null && imageUrl.endsWith(".svg")){
                Utility.loadSVGImage(context,contentDetails.getSnippet().getThumbnails().getHigh().getUrl(), youTubeThumbnailView);
            }else{
                Glide.with(context)
                        .load(contentDetails.getSnippet().getThumbnails().getHigh().getUrl())
                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.ic_image_loading_error)
                        .into(youTubeThumbnailView);
            }

            tvTitle.setText(contentDetails.getSnippet().getTitle());
        }

    }

    public interface PlaylistVideoRvInteractor{
        void onVideoClicked(VideoItemWrapper videoItemWrapper, String videoCode);
    }

}
