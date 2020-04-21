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
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class ProjectsRvAdapter extends RecyclerView.Adapter<ProjectsRvAdapter.ProjectsViewHolder> {

    private Context context;
    private List<ProjectsModel> projectsModels;
    private ProjectsRvInteractor listener;

    public ProjectsRvAdapter(Context context, ProjectsRvInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setProjects(List<ProjectsModel> projects){
        this.projectsModels = projects;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.projects_rv_adapter_layout, parent, false);
        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
        final ProjectsModel projectsModel = projectsModels.get(position);

        holder.bindView(projectsModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectClicked(projectsModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (projectsModels!=null){
            return projectsModels.size();
        }else{
            return 0;
        }
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvTitle;
        ImageView imgShare;

        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.project_image);
            tvTitle = itemView.findViewById(R.id.tv_title_project);
            imgShare = itemView.findViewById(R.id.img_share_project);

        }

        public void bindView(ProjectsModel projectsModel) {
            Glide.with(context)
                    .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
                    .override(150,130)
                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_image_loading_error)
                    .into(imageView);

            tvTitle.setText(projectsModel.getName());

        }
    }

    public interface ProjectsRvInteractor{
        void onProjectClicked(ProjectsModel projectsModel);
    }

}
