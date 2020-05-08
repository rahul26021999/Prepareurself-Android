package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

public class PlaylistItemAdapter extends RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder> {

    private Context context;
    private List<VideoItemWrapper> videoItemWrappers;
    private PlaylistItemListener listener;
    private int selectedPosition = 0;

    public PlaylistItemAdapter(Context context, PlaylistItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setVideoItemWrappers(List<VideoItemWrapper> videoItemWrappers){
        this.videoItemWrappers = videoItemWrappers;
    }

    public void setSelectedPosition(int position){
        selectedPosition = position;
    }

    public void onNextClicked(){
        selectedPosition = selectedPosition + 1;
        notifyDataSetChanged();
    }

    public void onPreviousClicked(){
        selectedPosition = selectedPosition -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item_player_adapter_layout, parent, false);
        return  new PlaylistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaylistItemViewHolder holder, final int position) {
        final VideoItemWrapper videoItemWrapper = videoItemWrappers.get(position);
        holder.bindView(videoItemWrapper);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                listener.onItemClicked(videoItemWrapper);
                notifyDataSetChanged();
            }
        });

        if (position == selectedPosition){
            holder.rootLayout.setBackgroundResource(R.color.DarkModePrimarySelectedBlack);
        }else{
            holder.rootLayout.setBackgroundResource(R.color.DarkModePrimaryBlack);
        }

    }

    @Override
    public int getItemCount() {
        if (videoItemWrappers!=null){
            return videoItemWrappers.size();
        }else{
            return 0;
        }
    }

    public class PlaylistItemViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        RelativeLayout rootLayout;
        TextView textView;

        public PlaylistItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_playlist_item_title);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            imageView = itemView.findViewById(R.id.image_playlist_item);
        }

        public void bindView(VideoItemWrapper videoItemWrapper){
            textView.setText(videoItemWrapper.getSnippet().getTitle());
            if (videoItemWrapper.getSnippet().getThumbnails().getMaxres()!=null){
                Glide.with(context).load(
                        videoItemWrapper.getSnippet().getThumbnails().getMaxres().getUrl())
                        .placeholder(R.drawable.placeholder)
                        .override(550,300)
                        .into(imageView);
            }else if (videoItemWrapper.getSnippet().getThumbnails().getHigh()!=null){
                Glide.with(context).load(
                        videoItemWrapper.getSnippet().getThumbnails().getHigh().getUrl())
                        .placeholder(R.drawable.placeholder)
                        .override(550,300)
                        .into(imageView);
            }else if (videoItemWrapper.getSnippet().getThumbnails().getMedium()!=null){
                Glide.with(context).load(
                        videoItemWrapper.getSnippet().getThumbnails().getMedium().getUrl())
                        .placeholder(R.drawable.placeholder)
                        .override(550,300)
                        .into(imageView);
            }else if (videoItemWrapper.getSnippet().getThumbnails().getStandard()!=null){
                Glide.with(context).load(
                        videoItemWrapper.getSnippet().getThumbnails().getStandard().getUrl())
                        .placeholder(R.drawable.placeholder)
                        .override(550,300)
                        .into(imageView);
            }else if (videoItemWrapper.getSnippet().getThumbnails().getDefaultThumbnail()!=null){
                Glide.with(context).load(
                        videoItemWrapper.getSnippet().getThumbnails().getDefaultThumbnail().getUrl())
                        .placeholder(R.drawable.placeholder)
                        .override(550,300)
                        .into(imageView);
            }

        }

    }

    public interface PlaylistItemListener{
        void onItemClicked(VideoItemWrapper videoItemWrapper);
    }

}
