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
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;


import java.util.List;

public class ResourcesRvAdapter extends RecyclerView.Adapter<ResourcesRvAdapter.ResourceViewHolder> {

    private Context context;
    private List<TopicsModel> topics;
    private int rotationAngle = 0;
    private int currentPosition = -1;
    private ResourceRvInteractor listener;

    public ResourcesRvAdapter(Context context, ResourceRvInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setTopics(List<TopicsModel> topics){
        this.topics = topics;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resources_rv_adapter_layout,parent, false);

        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResourceViewHolder holder, final int position) {
        final TopicsModel topicsModel = topics.get(position);
        holder.bindView(topicsModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResourceClicked(topicsModel);
            }
        });

//        if (currentPosition == position){
//            rotationAngle = rotationAngle == 0 ? 180 : 0;  //toggle
//            holder.imgExpansion.animate().rotation(rotationAngle).setDuration(500).start();
//        }
//
//        holder.imgExpansion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentPosition = position;
//                boolean expanded = topicsModel.getExpanded();
//                resource.setExpanded(!expanded);
//                notifyItemChanged(position);
//
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onResourceClicked(resource);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (topics!=null)
            return topics.size();
        else
            return 0;
    }

    public class ResourceViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView tvTitle;


        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
        }

        public void bindView(TopicsModel topicsModel) {
//            boolean expanded = topicsModel.getExpanded();
//
//            descriptionLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
//
//            tvResourceDescription.setText(topicsModel.getDescription());
//            tvResourceTitle.setText(topicsModel.getTitle());

            if (topicsModel.getImage_url()!=null && topicsModel.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context,Constants.TOPICSBASEURL + topicsModel.getImage_url(), imageView );
            }else{
                Glide.with(context)
                        .load(Constants.TOPICSBASEURL + topicsModel.getImage_url())
                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(imageView);
            }

            tvTitle.setText(topicsModel.getName());

        }
    }

    public interface ResourceRvInteractor{
        void onResourceClicked(TopicsModel topicsModel);
    }

}
