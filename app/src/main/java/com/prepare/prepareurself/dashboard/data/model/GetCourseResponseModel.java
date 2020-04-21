package com.prepare.prepareurself.dashboard.data.model;

import java.util.List;

public class GetCourseResponseModel {
    private int error_code;
    private List<CourseModel> courses = null;

    public GetCourseResponseModel() {
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<CourseModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses;
    }
}
