package com.prepare.prepareurself.dashboard.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
        View view = LayoutInflater.from(context).inflate(R.layout.project_viewtype_rv_layout,parent, false);

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

        TextView name,courseName,views,level;
        ImageView imageView;

        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            views=itemView.findViewById(R.id.views);
            imageView = itemView.findViewById(R.id.image_course_viewtype);
            level = itemView.findViewById(R.id.level);

        }

        public void bindView(Context context, ProjectsModel projectsModel){

            if (projectsModel.getImage_url()!=null && projectsModel.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context, Constants.PROJECTSIMAGEBASEURL+ projectsModel.getImage_url(), imageView);
            }else{
                Glide.with(context).load(
                        Constants.PROJECTSIMAGEBASEURL+ projectsModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                        .into(imageView);
            }
            name.setText(projectsModel.getName());
            if(projectsModel.getLevel().equals("hard")){
                level.setBackgroundTintList(context.getResources().getColorStateList(R.color.lightred));
            }
            else if(projectsModel.getLevel().equals("easy")){
                level.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            }
            else{
                level.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow));
            }
            Log.i("views", String.valueOf(projectsModel.getTotal_views()));
            views.setText(projectsModel.getTotal_views()+ " views");
            level.setText(projectsModel.getLevel());

        }
    }

    public interface ProjectsHorizontalRvListener {
        void onItemClicked(ProjectsModel projectsModel);
    }
}
