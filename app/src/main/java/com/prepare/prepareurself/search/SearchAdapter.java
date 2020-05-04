package com.prepare.prepareurself.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

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
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                View topicsView = LayoutInflater.from(context).inflate(R.layout.search_adapter_layout,parent, false);
                return new TopicsViewHolder(topicsView);

            case 2:
                View prjectsView = LayoutInflater.from(context).inflate(R.layout.search_adapter_layout,parent, false);
                return new ProjectsRvViewHolder(prjectsView);

            case 3:
                View resourcesView = LayoutInflater.from(context).inflate(R.layout.search_adapter_layout,parent, false);
                return new ResourcesViewHolder(resourcesView);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (searchRecyclerviewModels.get(position).getViewtype()){
            case 1:
                String topics_header = "Topics";
                final List<TopicsModel> topicsModels = searchRecyclerviewModels.get(position).getTopics();
                ((TopicsViewHolder)holder).bindView(context, topicsModels, topics_header, this);
                break;
            case 2:
                String projects_header = "Projects";
                final List<ProjectsModel> projectsModels = searchRecyclerviewModels.get(position).getProjects();
                ((ProjectsRvViewHolder)holder).bindView(context, projectsModels, projects_header, this);
                break;

            case 3:
                String resources_header = "Resources";
                final List<ResourceModel> resourceModels = searchRecyclerviewModels.get(position).getResources();
                ((ResourcesViewHolder)holder).bindView(context, resourceModels, resources_header, this);
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

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class TopicsViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private RecyclerView recyclerView;

        public TopicsViewHolder(View topicsView) {
            super(topicsView);
            textView = topicsView.findViewById(R.id.tv_type_search);
            recyclerView = topicsView.findViewById(R.id.search_type_rv);
        }

        public void bindView(Context context, List<TopicsModel> topicsModels, String header, DetailedSearchAdapter.DeatiledSearchListener listener) {
            textView.setText(header);
            DetailedSearchAdapter adapter = new DetailedSearchAdapter(context,listener);
            adapter.setTopicsModels(topicsModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
    }

    private class ProjectsRvViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private RecyclerView recyclerView;

        public ProjectsRvViewHolder(View prjectsView) {
            super(prjectsView);
            textView = prjectsView.findViewById(R.id.tv_type_search);
            recyclerView = prjectsView.findViewById(R.id.search_type_rv);
        }

        public void bindView(Context context, List<ProjectsModel> projectsModels, String header, DetailedSearchAdapter.DeatiledSearchListener listener) {
            DetailedSearchAdapter adapter = new DetailedSearchAdapter(context,listener);
            adapter.setProjectsModels(projectsModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
    }

    private class ResourcesViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private RecyclerView recyclerView;

        public ResourcesViewHolder(View resourcesView) {
            super(resourcesView);
            textView = resourcesView.findViewById(R.id.tv_type_search);
            recyclerView = resourcesView.findViewById(R.id.search_type_rv);
        }

        public void bindView(Context context, List<ResourceModel> resourceModels, String header, DetailedSearchAdapter.DeatiledSearchListener listener) {
            textView.setText(header);
            DetailedSearchAdapter adapter = new DetailedSearchAdapter(context,listener);
            adapter.setResourceModels(resourceModels);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

        }
    }

    public interface SearchListener{

    }


}
