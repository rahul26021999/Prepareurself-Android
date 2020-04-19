package com.prepare.prepareurself.Home.content.courses.data.model;

import android.graphics.drawable.Drawable;

public class PlaylistVideoItem {

    private String videoCode;
    private Drawable thumbnail;

    public PlaylistVideoItem(){

    }

    public String getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public Drawable getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail = thumbnail;
    }
}
