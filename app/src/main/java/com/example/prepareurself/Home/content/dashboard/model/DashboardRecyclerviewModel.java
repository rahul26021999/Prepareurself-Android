package com.example.prepareurself.Home.content.dashboard.model;


import java.util.ArrayList;

public class DashboardRecyclerviewModel {

    private int viewType;

    // course view type
    private int imageResource;
    private ArrayList<String> courseName;

    //add view type
    private String addText;

    public DashboardRecyclerviewModel(int viewType, int imageResource, ArrayList<String> courseName ) {
        this.imageResource = imageResource;
        this.courseName = courseName;
        this.viewType = viewType;
    }

    public DashboardRecyclerviewModel(int viewType,String addText) {
        this.addText = addText;
        this.viewType = viewType;
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

    public ArrayList<String> getCourseName() {
        return courseName;
    }

    public void setCourseName(ArrayList<String> courseName) {
        this.courseName = courseName;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
