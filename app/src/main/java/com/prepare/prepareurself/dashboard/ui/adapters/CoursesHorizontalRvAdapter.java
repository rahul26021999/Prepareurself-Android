package com.prepare.prepareurself.dashboard.ui.adapters;

import android.app.Activity;
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
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.Utility;

import java.util.List;

public class CoursesHorizontalRvAdapter extends RecyclerView.Adapter<CoursesHorizontalRvAdapter.CoursesHorizontalViewHolder> {

    Activity context;
    List<CourseModel> courses;
    private DashboardRvInteractor listener;

    public CoursesHorizontalRvAdapter(Activity context, DashboardRvInteractor listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoursesHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_viewtype_rv_layout,parent,false);
        return new CoursesHorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesHorizontalViewHolder holder, final int position) {
        holder.bindView(context,courses.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCourseClicked(courses.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (courses!=null)
            return courses.size();
        else
            return 0;
    }

    public void setCourses(List<CourseModel> courseModels) {
        this.courses = courseModels;
    }

    static class CoursesHorizontalViewHolder extends RecyclerView.ViewHolder{

        TextView tvCourseName;
        ImageView imageView;

        public CoursesHorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tv_course_name_viewtype);
            imageView = itemView.findViewById(R.id.image_course_viewtype);
        }

        public void bindView(Activity context, CourseModel course){

            if (course.getImage_url()!=null && course.getImage_url().endsWith(".svg")){
                Utility.loadSVGImage(context,Constants.COURSEIMAGEBASEUSRL+ course.getImage_url(),imageView);
            }else{
                Glide.with(context).load(
                        Constants.COURSEIMAGEBASEUSRL+ course.getImage_url())
                        .placeholder(R.drawable.placeholder)
                        .override(500,500)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(imageView);
            }

            tvCourseName.setText(course.getName());

        }
    }

    public interface DashboardRvInteractor{
        void onCourseClicked(CourseModel courseModel);
    }

}
