package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;

import java.util.List;

public class YoutubeSingleVideoResponseModel {

    private String kind;
    private String etag;
    private String nextPageToken;
    private String prevPageToken;
    private VideoItemsPageInfo pageInfo;
    private List<SingleVIdeoItemWrapper> items;

    public YoutubeSingleVideoResponseModel() {
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

    public List<SingleVIdeoItemWrapper> getItems() {
        return items;
    }

    public void setItems(List<SingleVIdeoItemWrapper> items) {
        this.items = items;
    }
}
