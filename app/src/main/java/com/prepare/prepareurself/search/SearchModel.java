package com.prepare.prepareurself.search;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

public class SearchModel {

    private List<TopicsModel> topics;
    private List<ResourceModel> resources;
    private List<ProjectsModel> projects;

    public SearchModel() {
    }

    public List<TopicsModel> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsModel> topics) {
        this.topics = topics;
    }

    public List<ResourceModel> getResources() {
        return resources;
    }

    public void setResources(List<ResourceModel> resources) {
        this.resources = resources;
    }

    public List<ProjectsModel> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectsModel> projects) {
        this.projects = projects;
    }
}
