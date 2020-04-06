package com.example.prepareurself.Home.content.dashboard.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prepareurself.R;

import java.util.ArrayList;

public class CoursesHorizontalRvAdapter extends RecyclerView.Adapter<CoursesHorizontalRvAdapter.CoursesHorizontalViewHolder> {

    Context context;
    ArrayList<String> courseNames;
    private DashboardRvInteractor listener;

    public CoursesHorizontalRvAdapter(Context context, ArrayList<String> courseNames, DashboardRvInteractor listener) {
        this.context = context;
        this.courseNames = courseNames;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoursesHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.courses_viewtype_rv_layout,parent,false);
        return new CoursesHorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesHorizontalViewHolder holder, int position) {
        holder.bindView(courseNames.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCourseClicked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseNames.size();
    }

    static class CoursesHorizontalViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourseName;

        public CoursesHorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tv_course_name_viewtype);
        }

        public void bindView(String courseName){
            tvCourseName.setText(courseName);
        }
    }

    public interface DashboardRvInteractor{
        void onCourseClicked();
    }

}
