package com.prepare.prepareurself.courses.data.model;

import com.prepare.prepareurself.dashboard.data.model.CourseModel;

public class CourseDetailReponseModel {

    private int error_code;
    private CourseModel course = null;
    private int rating = 0;
    private Boolean preference = false;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Boolean getPreference() {
        return preference;
    }

    public void setPreference(Boolean preference) {
        this.preference = preference;
    }
}
