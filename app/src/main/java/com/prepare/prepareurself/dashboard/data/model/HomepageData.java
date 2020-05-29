package com.prepare.prepareurself.dashboard.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;


@Entity(tableName = "home_page")
public class HomepageData {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "seeAll")
    @SerializedName("seeAll")
    private boolean seeAll;

    @ColumnInfo(name = "postedOn")
    @SerializedName("postedOn")
    private boolean postedOn;

    @ColumnInfo(name = "views")
    @SerializedName("views")
    private boolean views;

    @ColumnInfo(name = "likes")
    @SerializedName("likes")
    private boolean likes;

    @ColumnInfo(name = "courses")
    @SerializedName("courses")
    private List<CourseModel>  courses;

    @ColumnInfo(name = "project")
    @SerializedName("project")
    private List<ProjectsModel> project;

    @ColumnInfo(name = "resource")
    @SerializedName("resource")
    private List<ResourceModel> resource;

    @ColumnInfo(name = "course")
    @SerializedName("course")
    private CourseModel course;

    @ColumnInfo(name = "topics")
    @SerializedName("topics")
    private List<TopicsModel> topics;

    public HomepageData(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }

    public boolean isPostedOn() {
        return postedOn;
    }

    public void setPostedOn(boolean postedOn) {
        this.postedOn = postedOn;
    }

    public boolean isViews() {
        return views;
    }

    public void setViews(boolean views) {
        this.views = views;
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
