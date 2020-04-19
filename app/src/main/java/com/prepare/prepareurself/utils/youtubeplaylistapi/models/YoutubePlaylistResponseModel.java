package com.prepare.prepareurself.utils.youtubeplaylistapi.models;

import java.util.List;

public class YoutubePlaylistResponseModel {
    private String kind;
    private String etag;
    private String nextPageToken;
    private String prevPageToken;
    private VideoItemsPageInfo pageInfo;
    private List<VideoItemWrapper> items;

    public YoutubePlaylistResponseModel(){

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

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public VideoItemsPageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(VideoItemsPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<VideoItemWrapper> getItems() {
        return items;
    }

    public void setItems(List<VideoItemWrapper> items) {
        this.items = items;
    }
}
