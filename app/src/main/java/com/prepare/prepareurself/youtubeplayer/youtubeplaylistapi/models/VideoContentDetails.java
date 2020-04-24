package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;


public class VideoContentDetails {

    private String videoId;

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
