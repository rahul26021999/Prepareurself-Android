package com.prepare.prepareurself.dashboard.data.model;

import java.util.List;

public class GetSuggestedProjectsModel {

    private boolean success;
    private String course_id;
    private List<SuggestedProjectModel> projects;

    public GetSuggestedProjectsModel(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public List<SuggestedProjectModel> getProjects() {
        return projects;
    }

    public void setProjects(List<SuggestedProjectModel> projects) {
        this.projects = projects;
    }
}
