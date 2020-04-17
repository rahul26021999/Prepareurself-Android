package com.prepare.prepareurself.Home.content.resources.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TheoryResourcesRvAdapter extends RecyclerView.Adapter<TheoryResourcesRvAdapter.TheoryResourcesViewHolder> {
    Context context;
    List<ResourceModel> resourcesList;
    private TheoryResourceRvInteractor listener;

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
    public void onBindViewHolder(@NonNull final TheoryResourcesViewHolder holder, int position) {
        final ResourceModel theoryResources1= resourcesList.get(position);
        holder.bindview(theoryResources1);

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResourceClicked(theoryResources1);
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String encodedId = Utility.base64Encode(theoryResources1.getId());
                    Bitmap bitmap = Utility.getBitmapFromView(holder.imageView);
                    String text = theoryResources1.getTitle()+"\n"
                            +"http://prepareurself.tk/resource_theory/"+encodedId;
                    listener.onResourceShared(bitmap,text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        holder.hitLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                listener.OnLikeButtonClicked(theoryResources1,true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                listener.OnLikeButtonClicked(theoryResources1,false);
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

        private LikeButton hitLike;
        private ImageView imageView;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView imgShare;

        public TheoryResourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
            tvDescription = itemView.findViewById(R.id.tv_decription_topic);
            hitLike=itemView.findViewById(R.id.hitLike);
            imgShare = itemView.findViewById(R.id.img_share_theory_resource);

        }

        public  void bindview(final ResourceModel resourceModel){
            Glide.with(context).load(
                    Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url())
                    .placeholder(R.drawable.placeholder)
                    .override(300,280)
                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                    .error(R.drawable.ic_image_loading_error)
                    .into(imageView);

            tvTitle.setText(resourceModel.getTitle());
            tvDescription.setText(resourceModel.getDescription());


        }

    }

    public interface TheoryResourceRvInteractor{
        void onResourceClicked(ResourceModel resource);
        void OnLikeButtonClicked(ResourceModel resource,Boolean checked);
        void onResourceShared(Bitmap bitmap, String text);
    }


}
