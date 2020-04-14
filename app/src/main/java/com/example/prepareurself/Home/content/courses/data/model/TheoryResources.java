package com.example.prepareurself.Home.content.courses.data.model;

public class TheoryResources {
    private String imageUrl, theoryTitle, theoryId, contentUrl;

    public TheoryResources() {
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTheoryTitle() {
        return theoryTitle;
    }

    public String getTheoryId() {
        return theoryId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTheoryTitle(String theoryTitle) {
        this.theoryTitle = theoryTitle;
    }

    public void setTheoryId(String theoryId) {
        this.theoryId = theoryId;
    }
}
