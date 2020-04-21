package com.prepare.prepareurself.utils.youtubeplaylistapi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

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
            holder.textView.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
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
        TextView textView;

        public PlaylistItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.playlist_item_imageview);
            textView = itemView.findViewById(R.id.tv_playlist_item_title);
           // imageView.setVisibility(View.GONE);
        }

        public void bindView(VideoItemWrapper videoItemWrapper){
            textView.setText(videoItemWrapper.getSnippet().getTitle());
        }

    }

    public interface PlaylistItemListener{
        void onItemClicked(VideoItemWrapper videoItemWrapper);
    }

}
