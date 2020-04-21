package com.prepare.prepareurself.dashboard.data.model;


import java.util.List;

public class DashboardRecyclerviewModel {

    private int viewType;

    // course view type
    private int imageResource;
    private List<CourseModel> courses;
    private String categoryName;

    //add view type
    private String addText;

    public DashboardRecyclerviewModel(int viewType, String categoryName, List<CourseModel> courses) {
        this.categoryName = categoryName;
        this.courses = courses;
        this.viewType = viewType;
    }

    public DashboardRecyclerviewModel(int viewType,String addText) {
        this.addText = addText;
        this.viewType = viewType;
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
