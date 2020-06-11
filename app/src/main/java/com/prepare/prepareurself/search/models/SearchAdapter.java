package com.prepare.prepareurself.search.models;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.Constants;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter implements DetailedSearchAdapter.DeatiledSearchListener {

    private Context context;
    private SearchListener listener;

    private List<SearchRecyclerviewModel> searchRecyclerviewModels;

    public SearchAdapter(Context context, SearchListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<SearchRecyclerviewModel> searchRecyclerviewModels){
        this.searchRecyclerviewModels = searchRecyclerviewModels;
        notifyDataSetChanged();
    }

    public void addAll(List<SearchRecyclerviewModel> newList) {
        int lastIndex = searchRecyclerviewModels.size() - 1;
        searchRecyclerviewModels.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }

    public void clearData(){
        if (this.searchRecyclerviewModels!=null){
            this.searchRecyclerviewModels.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (searchRecyclerviewModels.get(position).getViewtype()){
            case 1 :
                return 1;
            case 2 :
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                View view = LayoutInflater.from(context).inflate(R.layout.searh_heading,parent, false);
                return new HeadingViewHolder(view);
                //break;

            case 2:

            case 3:

            case 4:
                View topicsView = LayoutInflater.from(context).inflate(R.layout.search_adapter_layout,parent, false);
                return new DetailedSearchViewHolder(topicsView);
            //  break;

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == searchRecyclerviewModels.size() - 1){
            listener.onBottomReached(position);
        }

        switch (searchRecyclerviewModels.get(position).getViewtype()){
            case 1:
                final String heading = searchRecyclerviewModels.get(position).getHeading();
                Log.d("haeding_debug", "onBindViewHolder: "+heading);
                ((HeadingViewHolder) holder).textView.setText(heading);
                break;
            case 2:
                final TopicsModel topicsModel = searchRecyclerviewModels.get(position).getTopicsModel();
                ((DetailedSearchViewHolder) holder).textView.setText(topicsModel.getName());
                Glide.with(context)
                        .load(Constants.TOPICSBASEURL + topicsModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .override(200,200)
                        .into(((DetailedSearchViewHolder) holder).imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onTopicsClickedFromSearch(topicsModel);
                    }
                });
                break;
            case 3:
                final ProjectsModel projectsModel = searchRecyclerviewModels.get(position).getProjectsModel();
                ((DetailedSearchViewHolder) holder).textView.setText(projectsModel.getName());
                ((DetailedSearchViewHolder) holder).tvDescription.setVisibility(View.VISIBLE);
                ((DetailedSearchViewHolder) holder).tvDescription.setText(Html.fromHtml(projectsModel.getDescription()));
                Glide.with(context)
                        .load(Constants.PROJECTSIMAGEBASEURL + projectsModel.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .override(200,200)
                        .error(R.drawable.placeholder)
                        .into(((DetailedSearchViewHolder) holder).imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onProjectClickedFromSearch(projectsModel);
                    }
                });
                break;
            case 4:
                final ResourceModel resourceModel = searchRecyclerviewModels.get(position).getResourceModel();
                ((DetailedSearchViewHolder) holder).textView.setText(resourceModel.getTitle());
                ((DetailedSearchViewHolder) holder).tvDescription.setVisibility(View.VISIBLE);
                ((DetailedSearchViewHolder) holder).tvDescription.setText(resourceModel.getDescription());
                if (resourceModel.getType().equalsIgnoreCase("theory")){
                    Glide.with(context)
                            .load(Constants.THEORYRESOURCEBASEURL + resourceModel.getImage_url())
                            .override(200,200)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(((DetailedSearchViewHolder) holder).imageView);
                }else if (resourceModel.getType().equalsIgnoreCase("video")){
                    Glide.with(context)
                            .load(R.mipmap.ic_action_video)
                            .override(200,200)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(((DetailedSearchViewHolder) holder).imageView);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onResourceClickedFromSearch(resourceModel);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (searchRecyclerviewModels!=null){
            return searchRecyclerviewModels.size();
        }else{
            return 0;
        }
    }


    public class DetailedSearchViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private ImageView imageView;
        private TextView tvDescription;

        public DetailedSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search_item);
            imageView = itemView.findViewById(R.id.img_search_item);
            tvDescription = itemView.findViewById(R.id.tv_search_description_item);

        }
    }

    @Override
    public void onProjectClicked(ProjectsModel projectsModel) {
        listener.onProjectClickedFromSearch(projectsModel);
    }

    @Override
    public void onTopicsClicked(TopicsModel topicsModel) {
        listener.onTopicsClickedFromSearch(topicsModel);
    }

    @Override
    public void onResourceClicked(ResourceModel resourceModel) {
        listener.onResourceClickedFromSearch(resourceModel);
    }

    public interface SearchListener{
        void onProjectClickedFromSearch(ProjectsModel projectsModel);
        void onTopicsClickedFromSearch(TopicsModel topicsModel);
        void onResourceClickedFromSearch(ResourceModel resourceModel);
        void onBottomReached(int position);
    }


    public class HeadingViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public HeadingViewHolder(View topicsView) {
            super(topicsView);
            textView = topicsView.findViewById(R.id.tv_type_search);

        }
    }
}
