package com.prepare.prepareurself.dashboard.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.DashboardRecyclerviewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;

import java.util.List;

import static com.prepare.prepareurself.utils.Constants.ADDVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.COURSEVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.PROJECTVIEWTYPE;
import static com.prepare.prepareurself.utils.Constants.TOPICVIEWTYPE;

public class DashboardRvAdapter extends RecyclerView.Adapter implements CoursesHorizontalRvAdapter.DashboardRvInteractor,
        TopicsHorizontalRvAdapter.TopicsHorizontalRvListener, ProjectsHorizontalRvAdapter.ProjectsHorizontalRvListener {

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

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (modelList.get(position).getViewType()){
            case COURSEVIEWTYPE :
                String categoryName = modelList.get(position).getCategoryName();
                final LiveData<List<CourseModel>> courses = modelList.get(position).getCourses();
                ((CourseViewHolder) holder).bindCoursesView(categoryName,courses, this);
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
                final LiveData<List<SuggestedTopicsModel>> topicsModels = modelList.get(position).getTopicsModels();
                ((TopicViewHolder) holder).bindCoursesView(modelList.get(position).getCategoryName(),topicsModels, this);
                ((TopicViewHolder) holder).tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (topicsModels.getValue()!=null)
                            listener.onTopicSeeAll(topicsModels.getValue().get(0).getCourse_id(), topicsModels.getValue().get(0).getName());
                    }
                });
                break;
            case PROJECTVIEWTYPE:
                final LiveData<List<SuggestedProjectModel>> projectModel = modelList.get(position).getProjectModels();
                ((ProjectViewHolder) holder).bindProjectsView(modelList.get(position).getCategoryName(),projectModel, this);
                ((ProjectViewHolder) holder).tvSeeAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (projectModel.getValue()!=null)
                            listener.onProjectSeeAll(projectModel.getValue().get(0).getCourse_id(), projectModel.getValue().get(0).getName());
                    }
                });
                break;
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

        public void bindCoursesView(String categoryName, LiveData<List<CourseModel>> courses, CoursesHorizontalRvAdapter.DashboardRvInteractor interactor){
            tvCourses.setText(categoryName);
            final CoursesHorizontalRvAdapter adapter = new CoursesHorizontalRvAdapter(context,interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);

            courses.observeForever(new Observer<List<CourseModel>>() {
                @Override
                public void onChanged(List<CourseModel> courseModels) {
                    if (courseModels!=null){
                        adapter.setCourses(courseModels);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

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

        public void bindCoursesView(String categoryName, LiveData< List<SuggestedTopicsModel>> topicsModels, TopicsHorizontalRvAdapter.TopicsHorizontalRvListener interactor){
            tvCourses.setText(categoryName);
            final TopicsHorizontalRvAdapter adapter = new TopicsHorizontalRvAdapter(context,interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);

            topicsModels.observeForever(new Observer<List<SuggestedTopicsModel>>() {
                @Override
                public void onChanged(List<SuggestedTopicsModel> topicsModels) {
                    if (topicsModels!=null){
                        adapter.setData(topicsModels);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

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

        public void bindProjectsView(String categoryName, final LiveData<List<SuggestedProjectModel>> projectModels, ProjectsHorizontalRvAdapter.ProjectsHorizontalRvListener interactor){
            tvCourses.setText(categoryName);
            final ProjectsHorizontalRvAdapter adapter = new ProjectsHorizontalRvAdapter(context,interactor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);

            projectModels.observeForever(new Observer<List<SuggestedProjectModel>>() {
                @Override
                public void onChanged(List<SuggestedProjectModel> p) {
                    if (p!=null){
                        adapter.setData(p);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder{

        TextView tvAdText;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdText = itemView.findViewById(R.id.tv_add_text);
        }

        public void bindAddView(String adText){
            tvAdText.setText(adText);
        }
    }

    @Override
    public void onCourseClicked(CourseModel courseModel) {
        listener.onCourseClicked(courseModel);
    }

    @Override
    public void onItemClicked(SuggestedTopicsModel topicsModel) {
        listener.onTopicClicked(topicsModel);
    }

    @Override
    public void onItemClicked(SuggestedProjectModel projectsModel) {
        listener.onProjectClicked(projectsModel);
    }

    public interface DashBoardInteractor{
        void onCourseClicked(CourseModel courseModel);
        void onTopicClicked(SuggestedTopicsModel topicsModel);
        void onProjectClicked(SuggestedProjectModel projectsModel);
        void onTopicSeeAll(int courseId, String name);
        void onProjectSeeAll(int courseId, String name);
        void onCourseSeeAll();
    }



}
