package com.prepare.prepareurself.dashboard.data.model;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

public class HomepageData {

    private String title;
    private String type;
    private boolean seeAll;
    private List<CourseModel> courses;
    private List<ProjectsModel> project;
    private List<ResourceModel> resource;
    private CourseModel course;
    private List<TopicsModel> topics;

    public HomepageData(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeeAll() {
        return seeAll;
    }

    public void setSeeAll(boolean seeAll) {
        this.seeAll = seeAll;
    }

    public List<CourseModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses;
    }

    public List<ProjectsModel> getProject() {
        return project;
    }

    public void setProject(List<ProjectsModel> project) {
        this.project = project;
    }

    public List<ResourceModel> getResource() {
        return resource;
    }

    public void setResource(List<ResourceModel> resource) {
        this.resource = resource;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public List<TopicsModel> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsModel> topics) {
        this.topics = topics;
    }
}
