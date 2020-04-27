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
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class TopicsHorizontalRvAdapter extends RecyclerView.Adapter<TopicsHorizontalRvAdapter.TopicsViewolder> {

    private Context context;
    private List<SuggestedTopicsModel> topicsModels;
    private TopicsHorizontalRvListener listener;

    public TopicsHorizontalRvAdapter(Context context, TopicsHorizontalRvListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicsViewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.courses_viewtype_rv_layout,parent, false);

        return new TopicsViewolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsViewolder holder, final int position) {
        holder.bindView(context, topicsModels.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(topicsModels.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (topicsModels !=null)
            return topicsModels.size();
        else
            return 0;
    }

    public void setData(List<SuggestedTopicsModel> topicsModels) {
        this.topicsModels = topicsModels;
    }

    class TopicsViewolder extends RecyclerView.ViewHolder {

        TextView tvCourseName;
        ImageView imageView;

        public TopicsViewolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tv_course_name_viewtype);
            imageView = itemView.findViewById(R.id.image_course_viewtype);

        }

        public void bindView(Context context, SuggestedTopicsModel topicsModel){

            Glide.with(context).load(
                    Constants.TOPICSBASEURL+ topicsModel.getImage_url())
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                    .error(R.drawable.placeholder)
                    .into(imageView);

            tvCourseName.setText(topicsModel.getName());

        }
    }

    public interface TopicsHorizontalRvListener{
        void onItemClicked(SuggestedTopicsModel topicsModel);
    }
}
