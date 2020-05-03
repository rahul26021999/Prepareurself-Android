package com.prepare.prepareurself.courses.data.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "projects")
public class ProjectsModel {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @Nullable
    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    private String type;

    @ColumnInfo(name = "level")
    @SerializedName("level")
    private String level;

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    private String image_url;

    @ColumnInfo(name = "link")
    @SerializedName("link")
    private String link;

    @Nullable
    @ColumnInfo(name = "playlist")
    @SerializedName("playlist")
    private  String playlist;

    @ColumnInfo(name = "course_id")
    @SerializedName("course_id")
    private int course_id;

    @ColumnInfo(name = "admin_id")
    @SerializedName("admin_id")
    private int admin_id;

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private String created_at;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private String updated_at;

    @ColumnInfo(name = "like")
    @SerializedName("like")
    private int like;

    @ColumnInfo(name = "total_likes")
    @SerializedName("total_likes")
    private int total_likes;

    @ColumnInfo(name = "view")
    @SerializedName("view")
    private int view;

    @ColumnInfo(name = "total_views")
    @SerializedName("total_views")
    private int total_views;

    @ColumnInfo(name = "sequence")
    @SerializedName("sequence")
    private int sequence;

    public int getLike() {
        return like;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getTotal_views() {
        return total_views;
    }

    public void setTotal_views(int total_views) {
        this.total_views = total_views;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
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
}
