package com.prepare.prepareurself.dashboard.ui.adapters;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class ProjectsHorizontalRvAdapter extends RecyclerView.Adapter<ProjectsHorizontalRvAdapter.ProjectsViewHolder> {

    private Context context;
    private List<ProjectsModel> projectsModels;
    private ProjectsHorizontalRvListener listener;

    public ProjectsHorizontalRvAdapter(Context context, ProjectsHorizontalRvListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.courses_viewtype_rv_layout,parent, false);

        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, final int position) {
        holder.bindView(context, projectsModels.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(projectsModels.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (projectsModels !=null)
            return projectsModels.size();
        else
            return 0;
    }

    public void setData(List<ProjectsModel> projectsModels) {
        this.projectsModels = projectsModels;
    }

    class ProjectsViewHolder extends RecyclerView.ViewHolder {

        TextView tvCourseName;
        ImageView imageView;

        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tv_course_name_viewtype);
            imageView = itemView.findViewById(R.id.image_course_viewtype);

        }

        public void bindView(Context context, ProjectsModel projectsModel){

            Glide.with(context).load(
                    Constants.PROJECTSIMAGEBASEURL+ projectsModel.getImage_url())
                    .placeholder(R.drawable.placeholder)
                    .override(500,500)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                    .error(R.drawable.placeholder)
                    .into(imageView);

            tvCourseName.setText(projectsModel.getName());

        }
    }

    public interface ProjectsHorizontalRvListener {
        void onItemClicked(ProjectsModel projectsModel);
    }
}
