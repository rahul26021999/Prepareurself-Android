package com.prepare.prepareurself.dashboard.data.model;


import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.courses.data.model.TopicsModel;

import java.util.List;

public class DashboardRecyclerviewModel {

    private int viewType;


    private String categoryName;

    // course view type
    private int imageResource;
    private LiveData<List<CourseModel>> courses;

    //topic viewtype
    private LiveData<List<TopicsModel>> topicsModels;

    //add view type
    private String addText;

    public DashboardRecyclerviewModel(int viewType, String categoryName, LiveData<List<CourseModel>> courses) {
        this.categoryName = categoryName;
        this.courses = courses;
        this.viewType = viewType;
    }

    public DashboardRecyclerviewModel(int viewType, LiveData< List<TopicsModel>> topicsModels, String categoryName) {
        this.viewType = viewType;
        this.categoryName = categoryName;
        this.topicsModels = topicsModels;
    }

    public DashboardRecyclerviewModel(int viewType, String addText) {
        this.addText = addText;
        this.viewType = viewType;
    }

    public LiveData<List<TopicsModel>> getTopicsModels() {
        return topicsModels;
    }

    public void setTopicsModels(LiveData<List<TopicsModel>> topicsModels) {
        this.topicsModels = topicsModels;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAddText() {
        return addText;
    }

    public void setAddText(String addText) {
        this.addText = addText;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public LiveData<List<CourseModel>> getCourses() {
        return courses;
    }

    public void setCourses(LiveData<List<CourseModel>> courses) {
        this.courses = courses;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
