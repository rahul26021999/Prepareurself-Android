package com.example.prepareurself.Home.ui.adapters;

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

    public CoursesHorizontalRvAdapter(Context context, ArrayList<String> courseNames) {
        this.context = context;
        this.courseNames = courseNames;
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

}
