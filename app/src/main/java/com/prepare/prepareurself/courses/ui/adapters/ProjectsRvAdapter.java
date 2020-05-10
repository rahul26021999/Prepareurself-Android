package com.prepare.prepareurself.courses.ui.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.transition.ChangeImageTransform;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.io.UnsupportedEncodingException;
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
    public void onBindViewHolder(@NonNull final ProjectsViewHolder holder, int position) {
        final ProjectsModel projectsModel = projectsModels.get(position);

        holder.bindView(projectsModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectClicked(projectsModel);
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String encodedId = Utility.base64EncodeForInt(projectsModel.getId());
                    Bitmap bitmap = Utility.getBitmapFromView(holder.imageView);
                    String text = projectsModel.getName()+"\n" +
                            "Prepareurself is providing various courses, projects and resources." +
                            "One place to learn skills and test them by developing projects.\n" +
                            "Checkout prepareurself app : \n" +
                            "prepareurself.in/project/"+encodedId;
                    listener.onProjectShared(bitmap,text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (projectsModel.getLike() == 0){
                    listener.onProjectLiked(projectsModel, 0);
                }else if (projectsModel.getLike() == 1){
                    listener.onProjectLiked(projectsModel, 1);
                }
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
        TextView tvTitle, tvNoOfLikes, tvNoOfViews;
        ImageView imgShare;
        ImageView likeButton;
        Chip tvLevel;

        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.project_image);
            tvTitle = itemView.findViewById(R.id.tv_title_project);
            imgShare = itemView.findViewById(R.id.img_share_project);
            likeButton = itemView.findViewById(R.id.spark_button);
            tvNoOfLikes = itemView.findViewById(R.id.tv_no_of_likes);
            tvNoOfViews = itemView.findViewById(R.id.no_of_views);
            tvLevel = itemView.findViewById(R.id.tv_level_project);

        }

        public void bindView(ProjectsModel projectsModel) {

            if(projectsModel.getImage_url()!=null && projectsModel.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context,Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url(), imageView);
            }else{
                Glide.with(context)
                        .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                        .placeholder(R.drawable.placeholder)
                        .into(imageView);
            }

            tvTitle.setText(projectsModel.getName());
            tvNoOfViews.setText(projectsModel.getTotal_views() + " views");
            tvNoOfLikes.setText(projectsModel.getTotal_likes() + " likes");

            if(projectsModel.getLevel().equals("hard"))
                tvLevel.setChipBackgroundColorResource(R.color.lightred);
            else if(projectsModel.getLevel().equals("easy"))
                tvLevel.setChipBackgroundColorResource(R.color.green);
            else
                tvLevel.setChipBackgroundColorResource(R.color.yellow);

            tvLevel.setText(projectsModel.getLevel());

            if (projectsModel.getLike() == 1){
                likeButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
            }else{
                likeButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
            }


        }
    }

    public interface ProjectsRvInteractor{
        void onProjectClicked(ProjectsModel projectsModel);
        void onProjectShared(Bitmap bitmap, String text);
        void onProjectLiked(ProjectsModel projectsModel, int liked);
    }

}
