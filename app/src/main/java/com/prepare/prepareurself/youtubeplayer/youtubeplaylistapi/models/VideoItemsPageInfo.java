package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models;

public class VideoItemsPageInfo {
    private int totalResults;
    private int resultsPerPage;

    public VideoItemsPageInfo(){

    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}
