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
        VideoResources v1 = videoResources.get(position);
        holder.bindView(v1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.videoClicked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoResources.size();
    }

    class VideoResourcesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvTitle;

        public VideoResourcesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_video_resources_adapter);
            tvTitle = itemView.findViewById(R.id.tv_video_resources_title);

        }

        public void bindView(VideoResources v1) {
            Glide.with(context)
                    .load(v1.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.person_placeholder)
                    .into(imageView);
            tvTitle.setText(v1.getVideoTitle());
        }
    }

    public interface VideoResourceInteractor{
        void videoClicked();
    }

}
