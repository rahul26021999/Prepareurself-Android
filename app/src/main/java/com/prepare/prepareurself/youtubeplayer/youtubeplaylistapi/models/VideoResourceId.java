package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;

public class VideoResourceId {
    private String kind;
    private String videoId;

    public VideoResourceId(){

    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
