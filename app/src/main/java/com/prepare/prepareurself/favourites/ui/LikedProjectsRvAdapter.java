package com.prepare.prepareurself.favourites.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.google.android.material.chip.Chip;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class LikedProjectsRvAdapter extends RecyclerView.Adapter<LikedProjectsRvAdapter.LikedProjectsViewHolder> {

    private Context context;
    private List<LikedProjectsModel> likedProjectsModels;
    private LikedProjectsRvInteractor listener;

    public LikedProjectsRvAdapter(Context context, LikedProjectsRvInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setLikedProjectsModels(List<LikedProjectsModel> likedProjectsModels){
        this.likedProjectsModels = likedProjectsModels;
    }

    @NonNull
    @Override
    public LikedProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.like_projects_rv_adapter_layout, parent, false);
        return new LikedProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LikedProjectsViewHolder holder, final int position) {
        final LikedProjectsModel projectsModel = likedProjectsModels.get(position);

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
                    String text = "Prepareurself is preparing me for my internships." +
                            "I found some best and amazing project work.\n" +
                            "Checkout them at :\n"+
                            "prepareurself.in/project/"+encodedId;
                    listener.onProjectShared(bitmap,text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {

            ValueAnimator buttonColorAnim = null;


            @Override
            public void onClick(View v) {

                if (buttonColorAnim == null && projectsModel.getLike() ==1){
                    holder.likeButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
                    listener.onProjectLiked(projectsModel,1);
                    holder.tvNoOfLikes.setText(projectsModel.getTotal_likes()-1 + " likes");
                    projectsModel.setTotal_likes(projectsModel.getTotal_likes()-1);
                    projectsModel.setLike(0);
                    Log.d("remove_debug", "onClick: "+position);
                    likedProjectsModels.remove(projectsModel);
                    notifyDataSetChanged();
                }else{
                    if(buttonColorAnim != null){
                        buttonColorAnim.reverse();
                        buttonColorAnim = null;
                        listener.onProjectLiked(projectsModel,1);
                        holder.tvNoOfLikes.setText(projectsModel.getTotal_likes() + " likes");
                    }
                    else {
                        final ImageView button = (ImageView) v;
                        buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), context.getResources().getColor(R.color.like_grey), context.getResources().getColor(R.color.like_blue));
                        buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                button.setColorFilter((Integer) animator.getAnimatedValue());
                            }
                        });
                        buttonColorAnim.start();
                        listener.onProjectLiked(projectsModel,0);
                        holder.tvNoOfLikes.setText(projectsModel.getTotal_likes()+1 + " likes");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.likedProjectsModels!=null){
            return this.likedProjectsModels.size();
        }else{
            return 0;
        }
    }

    public class LikedProjectsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvTitle, tvNoOfLikes, tvNoOfViews;
        ImageView imgShare;
        ImageView likeButton;
        Chip tvLevel;

        public LikedProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.project_image);
            tvTitle = itemView.findViewById(R.id.tv_title_project);
            imgShare = itemView.findViewById(R.id.img_share_project);
            likeButton = itemView.findViewById(R.id.spark_button);
            tvNoOfLikes = itemView.findViewById(R.id.tv_no_of_likes);
            tvNoOfViews = itemView.findViewById(R.id.no_of_views);
            tvLevel = itemView.findViewById(R.id.tv_level_project);

        }

        public void bindView(LikedProjectsModel projectsModel) {

            if(projectsModel.getImage_url()!=null && projectsModel.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context, Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url(), imageView);
            }else{
                Glide.with(context)
                        .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
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

    public interface LikedProjectsRvInteractor{
        void onProjectClicked(LikedProjectsModel projectsModel);
        void onProjectShared(Bitmap bitmap, String text);
        void onProjectLiked(LikedProjectsModel projectsModel, int liked);
    }


}
