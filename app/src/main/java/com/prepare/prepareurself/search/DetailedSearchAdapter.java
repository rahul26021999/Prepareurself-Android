package com.prepare.prepareurself.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import org.w3c.dom.Text;

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

        public DetailedSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search_item);
        }
    }

}
