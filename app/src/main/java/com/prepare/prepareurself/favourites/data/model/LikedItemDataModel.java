package com.prepare.prepareurself.favourites.data.model;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

public class LikedItemDataModel {
    private int id;
    private int user_id;
    private int resource_id;
    private int project_id;
    private ResourceModel resource;
    private ProjectsModel project;

    public LikedItemDataModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public ResourceModel getResource() {
        return resource;
    }

    public void setResource(ResourceModel resource) {
        this.resource = resource;
    }

    public ProjectsModel getProject() {
        return project;
    }

    public void setProject(ProjectsModel project) {
        this.project = project;
    }
}
