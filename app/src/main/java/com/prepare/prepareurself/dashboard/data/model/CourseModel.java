package com.prepare.prepareurself.dashboard.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "course")
public class CourseModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    private String image_url;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private String created_at;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private String updated_at;

    @ColumnInfo(name = "sequence")
    @SerializedName("sequence")
    private int sequence;

    @ColumnInfo(name = "logo_url")
    @SerializedName("logo_url")
    private String logo_url;

    @ColumnInfo(name = "color")
    @SerializedName("color")
    private String color;
    @ColumnInfo(name = "average_rating")
    @SerializedName("average_rating")
    private float average_rating;

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }
    public int getTopic_count() {
        return topic_count;
    }

    public void setTopic_count(int topic_count) {
        this.topic_count = topic_count;
    }

    public int getProject_count() {
        return project_count;
    }

    public void setProject_count(int project_count) {
        this.project_count = project_count;
    }

    @ColumnInfo(name = "topic_count")
    @SerializedName("topic_count")
    private int topic_count;

    @ColumnInfo(name = "project_count")
    @SerializedName("project_count")
    private int project_count;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String  getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
