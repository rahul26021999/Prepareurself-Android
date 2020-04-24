package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "playlistItemWrapper")
public class VideoItemWrapper {

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


    public VideoItemWrapper(){

    }

    public VideoSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(VideoSnippet snippet) {
        this.snippet = snippet;
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
