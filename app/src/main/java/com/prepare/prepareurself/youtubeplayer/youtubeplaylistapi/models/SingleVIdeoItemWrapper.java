package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "singlevideo")
public class SingleVIdeoItemWrapper {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "kind")
    @SerializedName("kind")
    private String kind;

    @ColumnInfo(name = "etag")
    @SerializedName("etag")
    private String etag;


    @SerializedName("contentDetails")
    @Embedded(prefix = "content_")
    private VideoContentDetails contentDetails;

    @SerializedName("snippet")
    @Embedded(prefix = "snippet_")
    private VideoSnippet snippet;

    public SingleVIdeoItemWrapper(){

    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public VideoContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(VideoContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public VideoSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(VideoSnippet snippet) {
        this.snippet = snippet;
    }
}
