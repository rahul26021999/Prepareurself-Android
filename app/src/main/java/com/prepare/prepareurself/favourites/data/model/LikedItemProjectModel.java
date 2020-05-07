package com.prepare.prepareurself.favourites.data.model;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

public class LikedItemProjectModel {
    private ProjectsModel project;

    public LikedItemProjectModel(){

    }


    public ProjectsModel getProject() {
        return project;
    }

    public void setProject(ProjectsModel project) {
        this.project = project;
    }
}
