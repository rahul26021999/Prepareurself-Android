package com.prepare.prepareurself.utils.youtubeplaylistapi.models;

import com.google.gson.annotations.SerializedName;

public class Thumbails {

    @SerializedName(value = "default")
    private DefaultThumbnail defaultThumbnail;

    @SerializedName(value = "medium")
    private MediumThumbnail medium;

    public Thumbails(){

    }


    public MediumThumbnail getMedium() {
        return medium;
    }

    public void setMedium(MediumThumbnail medium) {
        this.medium = medium;
    }

    public DefaultThumbnail getDefaultThumbnail() {
        return defaultThumbnail;
    }

    public void setDefaultThumbnail(DefaultThumbnail defaultThumbnail) {
        this.defaultThumbnail = defaultThumbnail;
    }
}
