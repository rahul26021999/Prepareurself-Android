package com.prepare.prepareurself.resources.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
                    String encodedId = Utility.base64EncodeForInt(theoryResources1.getId());
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
                listener.OnLikeButtonClicked(theoryResources1,0);
                holder.tvLikes.setText(theoryResources1.getTotal_likes()+1 + " likes");

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                listener.OnLikeButtonClicked(theoryResources1,1);
                holder.tvLikes.setText(theoryResources1.getTotal_likes() + " likes");
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
        private TextView tvViews, tvLikes;

        public TheoryResourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image);
            tvTitle = itemView.findViewById(R.id.tv_title_topic);
            tvDescription = itemView.findViewById(R.id.tv_decription_topic);
            hitLike=itemView.findViewById(R.id.hitLike);
            imgShare = itemView.findViewById(R.id.img_share_theory_resource);
            tvViews = itemView.findViewById(R.id.no_of_views);
            tvLikes = itemView.findViewById(R.id.tv_no_of_likes);

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
            tvViews.setText(resourceModel.getViews()+" views");
            tvLikes.setText(resourceModel.getTotal_likes() + " likes");

            if (resourceModel.getLike() == 1){
                hitLike.setLiked(true);
            }else{
                hitLike.setLiked(false);
            }

        }

    }

    public interface TheoryResourceRvInteractor{
        void onResourceClicked(ResourceModel resource);
        void OnLikeButtonClicked(ResourceModel resource,int checked);
        void onResourceShared(Bitmap bitmap, String text);
    }


}
