package com.prepare.prepareurself.resources.ui.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TheoryResourcesRvAdapter extends RecyclerView.Adapter<TheoryResourcesRvAdapter.TheoryResourcesViewHolder> {
    Context context;
    List<ResourceModel> resourcesList;
    private TheoryResourceRvInteractor listener;
    private boolean liked = false;

    public void setResourcesList(List<ResourceModel> resourcesList) {
        this.resourcesList = resourcesList;
    }


    public TheoryResourcesRvAdapter(Context context, TheoryResourceRvInteractor listener){
        this.context=context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TheoryResourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.theory_rv_adapter_layout,parent,false);
        return new TheoryResourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TheoryResourcesViewHolder holder, final int position) {
        final ResourceModel theoryResources1= resourcesList.get(position);
        holder.bindview(theoryResources1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResourceClicked(theoryResources1);
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String encodedId = Utility.base64EncodeForInt(theoryResources1.getId());
                    Bitmap bitmap = Utility.getBitmapFromView(holder.imageView);
                    String text = theoryResources1.getTitle()+"\n\n" +
                            "Prepareurself is providing various courses, projects and resources. " +
                            "One place to learn skills and test them by developing projects. \n" +
                            "Checkout prepareurself app : \n" +
                            "prepareurself.in/resource/"+encodedId;
                    listener.onResourceShared(bitmap,text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        holder.hitLike.setOnClickListener(new View.OnClickListener() {

            ValueAnimator buttonColorAnim = null;


            @Override
            public void onClick(View v) {

                if (theoryResources1.getLike() == 0){
                    listener.OnLikeButtonClicked(theoryResources1, 0);
                }else if (theoryResources1.getLike() == 1){
                    listener.OnLikeButtonClicked(theoryResources1, 1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (resourcesList!=null)
            return resourcesList.size();
        else
            return 0;
    }

    class TheoryResourcesViewHolder extends  RecyclerView.ViewHolder{

        private ImageView hitLike;
        private ImageView imageView;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView imgShare;
        private TextView tvViews, tvLikes;

        public TheoryResourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
            tvDescription = itemView.findViewById(R.id.tv_decription_topic);
            hitLike=itemView.findViewById(R.id.spark_button);
            imgShare = itemView.findViewById(R.id.img_share_theory_resource);
            tvViews = itemView.findViewById(R.id.no_of_views);
            tvLikes = itemView.findViewById(R.id.tv_no_of_likes);

        }

        public  void bindview(final ResourceModel resourceModel){

            if (resourceModel.getImage_url()!=null && resourceModel.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context,Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url(), imageView);
            }else{
                Glide.with(context).load(
                        Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                        .into(imageView);
            }

            tvTitle.setText(resourceModel.getTitle());
            tvDescription.setText(resourceModel.getDescription());
            tvViews.setText(resourceModel.getTotal_views()+" views");
            tvLikes.setText(resourceModel.getTotal_likes() + " likes");

            if (resourceModel.getLike() == 1){
                liked = true;
                hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_blue_24dp));
            }else{
                liked = false;
                hitLike.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_grey_24dp));
            }

        }

    }

    public interface TheoryResourceRvInteractor{
        void onResourceClicked(ResourceModel resource);
        void OnLikeButtonClicked(ResourceModel resource,int checked);
        void onResourceShared(Bitmap bitmap, String text);
    }


}
