package com.example.prepareurself.Home.content.courses.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prepareurself.Home.content.courses.model.Resource;
import com.example.prepareurself.R;

import java.util.List;

public class ResourcesRvAdapter extends RecyclerView.Adapter<ResourcesRvAdapter.ResourceViewHolder> {

    private Context context;
    private List<Resource> resources;
    private int rotationAngle = 0;
    private int currentPosition = -1;
    private ResourceRvInteractor listener;

    public ResourcesRvAdapter(Context context, ResourceRvInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResources(List<Resource> resources){
        this.resources = resources;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resource_rv_adapter_layout,parent, false);

        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResourceViewHolder holder, final int position) {
        final Resource resource = resources.get(position);
        holder.bindView(resource);

        if (currentPosition == position){
            rotationAngle = rotationAngle == 0 ? 180 : 0;  //toggle
            holder.imgExpansion.animate().rotation(rotationAngle).setDuration(500).start();
        }

        holder.imgExpansion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                boolean expanded = resource.getExpanded();
                resource.setExpanded(!expanded);
                notifyItemChanged(position);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResourceClicked(resource);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public class ResourceViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgExpansion;
        private TextView tvResourceDescription, tvResourceTitle;
        private ConstraintLayout descriptionLayout;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);

            imgExpansion = itemView.findViewById(R.id.img_resource_title_expansion);
            tvResourceDescription = itemView.findViewById(R.id.tv_resouce_description);
            tvResourceTitle = itemView.findViewById(R.id.tv_resource_title);
            descriptionLayout = itemView.findViewById(R.id.view_resource_decription_parent);

        }

        public void bindView(Resource resource) {
            boolean expanded = resource.getExpanded();

            descriptionLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);

            tvResourceDescription.setText(resource.getDescription());
            tvResourceTitle.setText(resource.getTitle());

        }
    }

    public interface ResourceRvInteractor{
        void onResourceClicked(Resource resource);
    }

}
