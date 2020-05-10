package com.prepare.prepareurself.dashboard.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProjectsHorizontalRvAdapter extends RecyclerView.Adapter<ProjectsHorizontalRvAdapter.ProjectsViewHolder> {

    private Context context;
    private List<ProjectsModel> projectsModels;
    private ProjectsHorizontalRvListener listener;
    private boolean isViews = false;
    private boolean isLikes = false;
    private boolean isPostedOn = false;

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

    public void setParams(boolean isViews, boolean isPostedOn, boolean isLikes){
        this.isViews = isViews;
        this.isPostedOn = isPostedOn;
        this.isLikes = isLikes;
    }

    class ProjectsViewHolder extends RecyclerView.ViewHolder {

        TextView name,courseName,views,level, likes, posteon;
        ImageView imageView;

        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            views=itemView.findViewById(R.id.views);
            imageView = itemView.findViewById(R.id.image_course_viewtype);
            level = itemView.findViewById(R.id.level);
            likes=  itemView.findViewById(R.id.likes);
            posteon =itemView.findViewById(R.id.postedon);

        }

        public void bindView(Context context, ProjectsModel projectsModel){

            if (projectsModel.getImage_url()!=null && projectsModel.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context, Constants.PROJECTSIMAGEBASEURL+ projectsModel.getImage_url(), imageView);
            }else{
//                Glide.with(context).load(
//                        Constants.PROJECTSIMAGEBASEURL+ projectsModel.getImage_url())
//                        .placeholder(R.drawable.placeholder)
//                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
//                        .into(imageView);

                Picasso.get()
                        .load(Constants.PROJECTSIMAGEBASEURL+ projectsModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
            }
            name.setText(projectsModel.getName());
            if (isViews){
                views.setVisibility(View.VISIBLE);
                views.setText(projectsModel.getTotal_views()+ " views");
            }
            else{
                views.setVisibility(View.GONE);
            }
            if (isLikes){
                likes.setVisibility(View.VISIBLE);
                likes.setText(projectsModel.getTotal_likes() + " likes");
            }else{
                likes.setVisibility(View.GONE);
            }

            if (isPostedOn){
                posteon.setVisibility(View.VISIBLE);
                String duration = Utility.getDurationBetweenTwoDays(projectsModel.getCreated_at().split(" ")[0]);
                if (!TextUtils.isEmpty(duration)){
                    posteon.setText(duration);
                }else{
                    posteon.setVisibility(View.GONE);
                }
            }else{
                posteon.setVisibility(View.GONE);
            }

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
