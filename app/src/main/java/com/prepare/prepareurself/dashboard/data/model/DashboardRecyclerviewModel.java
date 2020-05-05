package com.prepare.prepareurself.dashboard.data.model;


import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

public class DashboardRecyclerviewModel {

    private int viewType;


    private String categoryName;

    // course view type
    private int imageResource;
    private List<CourseModel> courses;

    //topic viewtype
    private List<TopicsModel> topicsModels;

    private List<ProjectsModel> projectModels;

    private List<ResourceModel> resourceModels;

    //add view type
    private String addText;

    private boolean seeAll;

    public DashboardRecyclerviewModel(int viewType, String categoryName, boolean seeAll,List<CourseModel> courses) {
        this.categoryName = categoryName;
        this.courses = courses;
        this.viewType = viewType;
        this.seeAll =seeAll;
    }

    public DashboardRecyclerviewModel(int viewType, List<TopicsModel> topicsModels, String categoryName, boolean seeAll) {
        this.viewType = viewType;
        this.categoryName = categoryName;
        this.topicsModels = topicsModels;
        this.seeAll = seeAll;
    }

    public DashboardRecyclerviewModel(List<ProjectsModel> projectModels, int viewType, String categoryName, boolean seeAll){
        this.projectModels = projectModels;
        this.viewType = viewType;
        this.categoryName = categoryName;
        this.seeAll = seeAll;
    }

    public DashboardRecyclerviewModel(int viewType, String categoryName, List<ResourceModel> resourceModels, boolean seeAll) {
        this.viewType = viewType;
        this.categoryName = categoryName;
        this.resourceModels = resourceModels;
        this.seeAll = seeAll;
    }

    public DashboardRecyclerviewModel(int viewType, String addText) {
        this.addText = addText;
        this.viewType = viewType;
    }

    public List<ResourceModel> getResourceModels() {
        return resourceModels;
    }

    public void setResourceModels(List<ResourceModel> resourceModels) {
        this.resourceModels = resourceModels;
    }

    public boolean isSeeAll() {
        return seeAll;
    }

    public void setSeeAll(boolean seeAll) {
        this.seeAll = seeAll;
    }

    public List<TopicsModel> getTopicsModels() {
        return topicsModels;
    }

    public void setTopicsModels(List<TopicsModel> topicsModels) {
        this.topicsModels = topicsModels;
    }

    public List<ProjectsModel> getProjectModels() {
        return projectModels;
    }

    public void setProjectModels(List<ProjectsModel> projectModels) {
        this.projectModels = projectModels;
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

    public List<CourseModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
