package com.prepare.prepareurself.dashboard.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.DashboardRecyclerviewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

import static com.prepare.prepareurself.utils.Constants.ADDVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.COURSEVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.PROJECTVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.RESOURCEVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.TOPICVIEWTYPE;

public class DashboardRvAdapter extends RecyclerView.Adapter implements CoursesHorizontalRvAdapter.DashboardRvInteractor,
        TopicsHorizontalRvAdapter.TopicsHorizontalRvListener, ProjectsHorizontalRvAdapter.ProjectsHorizontalRvListener,
        ResourceRvHorizontalAdapter.ResourceHomePageListener {

    private List<DashboardRecyclerviewModel> modelList;
    private Activity context;
    private DashBoardInteractor listener;

    public DashboardRvAdapter(Activity context, DashBoardInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<DashboardRecyclerviewModel> modelList){
        this.modelList = modelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (modelList.get(position).getViewType()){
            case COURSEVIEWTYPE :
                return COURSEVIEWTYPE;
            case ADDVIEWTYPE :
                return ADDVIEWTYPE;
            case TOPICVIEWTYPE:
                return TOPICVIEWTYPE;
            case PROJECTVIEWTYPE:
                return PROJECTVIEWTYPE;
            case RESOURCEVIEWTYPE:
                return RESOURCEVIEWTYPE;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case COURSEVIEWTYPE :
                View courseView = LayoutInflater.from(context).inflate(R.layout.course_header_rv_viewtype_layout,parent, false);
                return new CourseViewHolder(courseView);

            case ADDVIEWTYPE :
                View addView = LayoutInflater.from(context).inflate(R.layout.add_viewtype_tv_layout,parent, false);
                return new AddViewHolder(addView);

            case TOPICVIEWTYPE:
                View topicView = LayoutInflater.from(context).inflate(R.layout.course_header_rv_viewtype_layout,parent, false);
                return new TopicViewHolder(topicView);

            case PROJECTVIEWTYPE:
                View projectView = LayoutInflater.from(context).inflate(R.layout.course_header_rv_viewtype_layout,parent, false);
                return new ProjectViewHolder(projectView);
            case RESOURCEVIEWTYPE:
                View resourceView = LayoutInflater.from(context).inflate(R.layout.course_header_rv_viewtype_layout, parent, false);
                return new ResourceHomeViewHolder(resourceView);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (modelList.get(position).getViewType()){
            case COURSEVIEWTYPE :
                String categoryName = modelList.get(position).getCategoryName();
                final List<CourseModel> courses = modelList.get(position).getCourses();
                ((CourseViewHolder) holder).bindCoursesView(categoryName,courses, this);
                if (modelList.get(position).isSeeAll()){
                    ((CourseViewHolder) holder).tvSeeAll.setVisibility(View.VISIBLE);
                }else{
                    ((CourseViewHolder) holder).tvSeeAll.setVisibility(View.GONE);
                }
                ((CourseViewHolder) holder).tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCourseSeeAll();
                    }
                });
                break;
            case ADDVIEWTYPE :
                String adText = modelList.get(position).getAddText();
                ((AddViewHolder) holder).bindAddView(adText);
                break;
            case TOPICVIEWTYPE:
                final List<TopicsModel> topicsModels = modelList.get(position).getTopicsModels();
                ((TopicViewHolder) holder).bindCoursesView(modelList.get(position).getCategoryName(),topicsModels, this);
                if (modelList.get(position).isSeeAll()){
                    ((TopicViewHolder) holder).tvSeeAll.setVisibility(View.VISIBLE);
                }else{
                    ((TopicViewHolder) holder).tvSeeAll.setVisibility(View.GONE);
                }
                ((TopicViewHolder) holder).tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (topicsModels!=null)
                            listener.onTopicSeeAll(topicsModels.get(0).getCourse_id(), topicsModels.get(0).getName());
                    }
                });
                break;
            case PROJECTVIEWTYPE:
                final List<ProjectsModel> projectModel = modelList.get(position).getProjectModels();
                ((ProjectViewHolder) holder).bindProjectsView(modelList.get(position).getCategoryName(),projectModel, this);
                if (modelList.get(position).isSeeAll()){
                    ((ProjectViewHolder) holder).tvSeeAll.setVisibility(View.VISIBLE);
                }else{
                    ((ProjectViewHolder) holder).tvSeeAll.setVisibility(View.GONE);
                }
                ((ProjectViewHolder) holder).tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (projectModel!=null)
                            listener.onProjectSeeAll(projectModel.get(0).getCourse_id(), projectModel.get(0).getName());
                    }
                });
                break;
            case RESOURCEVIEWTYPE:
                final List<ResourceModel> resourceModels = modelList.get(position).getResourceModels();
                ((ResourceHomeViewHolder) holder).bindResourceView(modelList.get(position).getCategoryName(),resourceModels,this);
                if (modelList.get(position).isSeeAll()){
                    ((ResourceHomeViewHolder) holder).tvSeeAll.setVisibility(View.VISIBLE);
                }else{
                    ((ResourceHomeViewHolder) holder).tvSeeAll.setVisibility(View.GONE);
                }
                ((ResourceHomeViewHolder) holder).tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (resourceModels!=null){
                            listener.onResourceSeeAllClicked(resourceModels.get(0).getCourse_topic_id());
                        }
                    }
                });
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        if (modelList!=null){
            return modelList.size();
        }else{
            return 0;
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourses, tvSeeAll;
        RecyclerView rvCourses;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourses = itemView.findViewById(R.id.tv_courses_header_viewtype);
            rvCourses = itemView.findViewById(R.id.rv_courses_dashboard);
            tvSeeAll  =itemView.findViewById(R.id.tv_see_all);
        }

        public void bindCoursesView(String categoryName, List<CourseModel> courses, CoursesHorizontalRvAdapter.DashboardRvInteractor interactor){
            tvCourses.setText(categoryName);
            final CoursesHorizontalRvAdapter adapter = new CoursesHorizontalRvAdapter(context,interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);
            adapter.setCourses(courses);
            adapter.notifyDataSetChanged();

        }
    }

    class TopicViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourses, tvSeeAll;
        RecyclerView rvCourses;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourses = itemView.findViewById(R.id.tv_courses_header_viewtype);
            rvCourses = itemView.findViewById(R.id.rv_courses_dashboard);
            tvSeeAll  =itemView.findViewById(R.id.tv_see_all);
        }

        public void bindCoursesView(String categoryName,  List<TopicsModel> topicsModels, TopicsHorizontalRvAdapter.TopicsHorizontalRvListener interactor){
            tvCourses.setText(categoryName);
            final TopicsHorizontalRvAdapter adapter = new TopicsHorizontalRvAdapter(context,interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);
            adapter.setData(topicsModels);
            adapter.notifyDataSetChanged();
        }
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourses, tvSeeAll;
        RecyclerView rvCourses;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourses = itemView.findViewById(R.id.tv_courses_header_viewtype);
            rvCourses = itemView.findViewById(R.id.rv_courses_dashboard);
            tvSeeAll  =itemView.findViewById(R.id.tv_see_all);
        }

        public void bindProjectsView(String categoryName, final List<ProjectsModel> projectModels, ProjectsHorizontalRvAdapter.ProjectsHorizontalRvListener interactor){
            tvCourses.setText(categoryName);
            final ProjectsHorizontalRvAdapter adapter = new ProjectsHorizontalRvAdapter(context,interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);
            adapter.setData(projectModels);
            adapter.notifyDataSetChanged();

        }
    }

    private class ResourceHomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourses, tvSeeAll;
        RecyclerView rvCourses;

        public ResourceHomeViewHolder(View resourceView) {
            super(resourceView);

            tvCourses = itemView.findViewById(R.id.tv_courses_header_viewtype);
            rvCourses = itemView.findViewById(R.id.rv_courses_dashboard);
            tvSeeAll = itemView.findViewById(R.id.tv_see_all);

        }

        public void bindResourceView(String categoryName, final List<ResourceModel> resourceModels, ResourceRvHorizontalAdapter.ResourceHomePageListener interactor) {
            tvCourses.setText(categoryName);
            final ResourceRvHorizontalAdapter adapter = new ResourceRvHorizontalAdapter(context, interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);

            adapter.setResourceModels(resourceModels);
            adapter.notifyDataSetChanged();


        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder{

        AdView mAdView;
        View adContainer;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            adContainer = itemView.findViewById(R.id.adViewdashboardrv);
            mAdView = new AdView(context);

            mAdView.setAdSize(AdSize.LARGE_BANNER);
            mAdView.setBackgroundResource(R.color.colorPrimaryLight);
            mAdView.setAdUnitId(context.getResources().getString(R.string.bannerAds));
            ((RelativeLayout)adContainer).addView(mAdView);

        }

        public void bindAddView(String adText){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    adContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });
        }
    }

    @Override
    public void onCourseClicked(CourseModel courseModel) {
        listener.onCourseClicked(courseModel);
    }

    @Override
    public void onItemClicked(TopicsModel topicsModel) {
        listener.onTopicClicked(topicsModel);
    }

    @Override
    public void onItemClicked(ProjectsModel projectsModel) {
        listener.onProjectClicked(projectsModel);
    }

    @Override
    public void onResourceHomeClicked(ResourceModel resourceModel) {
        listener.onResourceClicked(resourceModel);
    }

    public interface DashBoardInteractor{
        void onCourseClicked(CourseModel courseModel);
        void onTopicClicked(TopicsModel topicsModel);
        void onProjectClicked(ProjectsModel projectsModel);
        void onTopicSeeAll(int courseId, String name);
        void onProjectSeeAll(int courseId, String name);
        void onCourseSeeAll();
        void onResourceClicked(ResourceModel resourceModel);
        void onResourceSeeAllClicked(int course_topic_id);
    }



}
