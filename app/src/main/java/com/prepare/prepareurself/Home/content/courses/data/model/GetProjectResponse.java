package com.prepare.prepareurself.Home.content.courses.data.model;

public class GetProjectResponse {

    private int error_code;
    private ProjectResponse Project;

    public GetProjectResponse(){

    }

    public ProjectResponse getProject() {
        return Project;
    }

    public void setProject(ProjectResponse project) {
        this.Project = project;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }


}
