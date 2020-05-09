package com.prepare.prepareurself.search.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.Constants;

import java.util.List;

public class DetailedSearchAdapter extends RecyclerView.Adapter<DetailedSearchAdapter.DetailedSearchViewHolder> {

    private Context context;
    private List<TopicsModel> topicsModels;
    private List<ProjectsModel> projectsModels;
    private List<ResourceModel> resourceModels;
    private DeatiledSearchListener listener;
    private int type;

    public interface DeatiledSearchListener{
        void onProjectClicked(ProjectsModel projectsModel);
        void onTopicsClicked(TopicsModel topicsModel);
        void onResourceClicked(ResourceModel resourceModel);
    }

    public DetailedSearchAdapter(Context context, DeatiledSearchListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setTopicsModels(List<TopicsModel> topicsModels){
        this.topicsModels = topicsModels;
        this.type = 1;
        notifyDataSetChanged();
    }

    public void  setProjectsModels(List<ProjectsModel> projectsModels){
        this.projectsModels = projectsModels;
        this.type = 2;
        notifyDataSetChanged();
    }

    public void setResourceModels(List<ResourceModel> resourceModels){
        this.resourceModels = resourceModels;
        this.type = 3;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailedSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detailed_search_viewholder, parent, false);
        return new DetailedSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedSearchViewHolder holder, int position) {
        switch (type){
            case 1 :
                final TopicsModel topicsModel = topicsModels.get(position);
                holder.textView.setText(topicsModel.getName());
                Glide.with(context)
                        .load(Constants.TOPICSBASEURL + topicsModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onTopicsClicked(topicsModel);
                    }
                });
                break;
            case 2:
                final ProjectsModel projectsModel = projectsModels.get(position);
                holder.textView.setText(projectsModel.getName());
                Glide.with(context)
                        .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onProjectClicked(projectsModel);
                    }
                });
                break;
            case 3:
                final ResourceModel resourceModel = resourceModels.get(position);
                holder.textView.setText(resourceModel.getTitle());
                holder.tvDescription.setVisibility(View.VISIBLE);
                holder.tvDescription.setText(resourceModel.getDescription());
                if (resourceModel.getType().equalsIgnoreCase("theory")){
                    Glide.with(context)
                            .load(Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(holder.imageView);
                }else if (resourceModel.getType().equalsIgnoreCase("video")){
                    Glide.with(context)
                            .load(R.mipmap.ic_action_video)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(holder.imageView);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onResourceClicked(resourceModel);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (type){
            case 1 :
                return topicsModels.size();
            case 2:
                return projectsModels.size();
            case 3:
                return resourceModels.size();
            default:
                return 0;
        }
    }

    public class DetailedSearchViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;
        private TextView tvDescription;

        public DetailedSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search_item);
            imageView = itemView.findViewById(R.id.img_search_item);
            tvDescription = itemView.findViewById(R.id.tv_search_description_item);

        }
    }

}
