package com.prepare.prepareurself.utils.youtubeplaylistapi.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity( tableName = "playlistVideos")
public class VideoContentDetails {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "videoId")
    @SerializedName("videoId")
    private String videoId;

    @ColumnInfo(name = "videoPublishedAt")
    @SerializedName("videoPublishedAt")
    private String videoPublishedAt;

    public VideoContentDetails() {
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoPublishedAt() {
        return videoPublishedAt;
    }

    public void setVideoPublishedAt(String videoPublishedAt) {
        this.videoPublishedAt = videoPublishedAt;
    }
}
