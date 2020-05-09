package com.prepare.prepareurself.search.models;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerviewModel {

    private String heading;
    private TopicsModel topicsModel;
    private ResourceModel resourceModel;
    private ProjectsModel projectsModel;
    private int viewtype;

    public SearchRecyclerviewModel(String heading, int viewtype) {
        this.heading = heading;
        this.viewtype = viewtype;
    }

    public SearchRecyclerviewModel(TopicsModel topicsModel, int viewtype) {
        this.topicsModel = topicsModel;
        this.viewtype = viewtype;
    }

    public SearchRecyclerviewModel(ResourceModel resourceModel, int viewtype) {
        this.resourceModel = resourceModel;
        this.viewtype = viewtype;
    }

    public SearchRecyclerviewModel(ProjectsModel projectsModel, int viewtype) {
        this.projectsModel = projectsModel;
        this.viewtype = viewtype;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public TopicsModel getTopicsModel() {
        return topicsModel;
    }

    public void setTopicsModel(TopicsModel topicsModel) {
        this.topicsModel = topicsModel;
    }

    public ResourceModel getResourceModel() {
        return resourceModel;
    }

    public void setResourceModel(ResourceModel resourceModel) {
        this.resourceModel = resourceModel;
    }

    public ProjectsModel getProjectsModel() {
        return projectsModel;
    }

    public void setProjectsModel(ProjectsModel projectsModel) {
        this.projectsModel = projectsModel;
    }

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }
}
