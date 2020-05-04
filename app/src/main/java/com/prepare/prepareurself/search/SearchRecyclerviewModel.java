package com.prepare.prepareurself.search;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

public class SearchRecyclerviewModel {

    private int viewtype;

    private List<TopicsModel> topics;
    private List<ProjectsModel> projects;
    private List<ResourceModel> resources;

    private String heading;

    public SearchRecyclerviewModel(int viewtype, String heading, List<TopicsModel> topics) {
        this.viewtype = viewtype;
        this.topics = topics;
        this.heading = heading;
    }

    public SearchRecyclerviewModel(List<ProjectsModel> projects, String heading, int viewtype) {
        this.viewtype = viewtype;
        this.projects = projects;
        this.heading = heading;
    }

    public SearchRecyclerviewModel(int viewtype, List<ResourceModel> resources, String heading) {
        this.viewtype = viewtype;
        this.resources = resources;
        this.heading = heading;
    }

    public int getViewtype() {
        return viewtype;
    }

    public void setViewtype(int viewtype) {
        this.viewtype = viewtype;
    }

    public List<TopicsModel> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsModel> topics) {
        this.topics = topics;
    }

    public List<ProjectsModel> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectsModel> projects) {
        this.projects = projects;
    }

    public List<ResourceModel> getResources() {
        return resources;
    }

    public void setResources(List<ResourceModel> resources) {
        this.resources = resources;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
