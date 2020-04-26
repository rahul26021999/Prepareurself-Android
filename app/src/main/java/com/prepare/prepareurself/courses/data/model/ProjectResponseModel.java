package com.prepare.prepareurself.courses.data.model;

public class ProjectResponseModel {

    private boolean success;
    private int error_code;
    private ProjectsModel project;

    public ProjectResponseModel(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ProjectsModel getProject() {
        return project;
    }

    public void setProject(ProjectsModel project) {
        this.project = project;
    }
}
