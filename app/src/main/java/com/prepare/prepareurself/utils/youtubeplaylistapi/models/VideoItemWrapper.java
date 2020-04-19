package com.prepare.prepareurself.utils.youtubeplaylistapi.models;

import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;

import java.util.List;

public class VideoItemWrapper {
    private String kind;
    private String etag;
    private String id;
    public VideoContentDetails contentDetails;

    public VideoItemWrapper(){

    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(VideoContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }
}
