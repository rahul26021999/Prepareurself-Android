package com.prepare.prepareurself.search.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

@Entity(tableName = "searchitems")
public class SearchModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "topics")
    @SerializedName("topics")
    private List<TopicsModel> topics;

    @ColumnInfo(name = "resource")
    @SerializedName("resource")
    private List<ResourceModel> resource;

    @ColumnInfo(name = "projects")
    @SerializedName("projects")
    private List<ProjectsModel> projects;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    public SearchModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TopicsModel> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsModel> topics) {
        this.topics = topics;
    }

    public List<ResourceModel> getResource() {
        return resource;
    }

    public void setResource(List<ResourceModel> resource) {
        this.resource = resource;
    }

    public List<ProjectsModel> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectsModel> projects) {
        this.projects = projects;
    }
}
