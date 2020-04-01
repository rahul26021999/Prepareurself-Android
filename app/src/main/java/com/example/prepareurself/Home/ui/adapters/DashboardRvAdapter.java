package com.example.prepareurself.Home.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prepareurself.Home.model.DashboardRecyclerviewModel;
import com.example.prepareurself.R;
import com.example.prepareurself.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.example.prepareurself.utils.Constants.ADDVIEWTYPE;
import static com.example.prepareurself.utils.Constants.COURSEVIEWTYPE;

public class DashboardRvAdapter extends RecyclerView.Adapter {

    private List<DashboardRecyclerviewModel> modelList;
    private Context context;

    public DashboardRvAdapter(List<DashboardRecyclerviewModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (modelList.get(position).getViewType()){
            case COURSEVIEWTYPE :
                return COURSEVIEWTYPE;
            case ADDVIEWTYPE :
                return ADDVIEWTYPE;
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
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (modelList.get(position).getViewType()){
            case COURSEVIEWTYPE :
                int courseImage = modelList.get(position).getImageResource();
                ArrayList<String> courses = modelList.get(position).getCourseName();
                ((CourseViewHolder) holder).bindCoursesView(courseImage,courses);
                break;
            case ADDVIEWTYPE :
                String adText = modelList.get(position).getAddText();
                ((AddViewHolder) holder).bindAddView(adText);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourses;
        RecyclerView rvCourses;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourses = itemView.findViewById(R.id.tv_courses_header_viewtype);
            rvCourses = itemView.findViewById(R.id.rv_courses_dashboard);
        }

        public void bindCoursesView(int courseImage, ArrayList<String> courses){
            CoursesHorizontalRvAdapter adapter = new CoursesHorizontalRvAdapter(context,courses);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            rvCourses.setLayoutManager(layoutManager);
            rvCourses.setAdapter(adapter);
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

}
