package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;

import com.google.gson.annotations.SerializedName;

public class Thumbails {

    @SerializedName(value = "default")
    private DefaultThumbnail defaultThumbnail;

    @SerializedName(value = "medium")
    private MediumThumbnail medium;

    @SerializedName(value = "high")
    private HighThumbnail high;

    @SerializedName(value = "standard")
    private StandardThumbnail standard;

    @SerializedName(value = "maxres")
    private MaxResThumbnail maxres;

    public Thumbails(){

    }

    public HighThumbnail getHigh() {
        return high;
    }

    public void setHigh(HighThumbnail high) {
        this.high = high;
    }

    public StandardThumbnail getStandard() {
        return standard;
    }

    public void setStandard(StandardThumbnail standard) {
        this.standard = standard;
    }

    public MaxResThumbnail getMaxres() {
        return maxres;
    }

    public void setMaxres(MaxResThumbnail maxres) {
        this.maxres = maxres;
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
