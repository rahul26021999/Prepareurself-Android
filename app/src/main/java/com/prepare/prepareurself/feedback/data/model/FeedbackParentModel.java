package com.prepare.prepareurself.feedback.data.model;

public class FeedbackParentModel {

    private int viewType;
    private FeedbackFourOptionsModel feedbackFourOptionsModel;
    private FeedbackTwoOptionsModel feedbackTwoOptionsModel;
    private FeedbackInoutModel feedbackInoutModel;

    public FeedbackParentModel(int viewType, FeedbackFourOptionsModel feedbackFourOptionsModel) {
        this.viewType = viewType;
        this.feedbackFourOptionsModel = feedbackFourOptionsModel;
    }

    public FeedbackParentModel(int viewType, FeedbackTwoOptionsModel feedbackTwoOptionsModel) {
        this.viewType = viewType;
        this.feedbackTwoOptionsModel = feedbackTwoOptionsModel;
    }

    public FeedbackParentModel(int viewType, FeedbackInoutModel feedbackInoutModel) {
        this.viewType = viewType;
        this.feedbackInoutModel = feedbackInoutModel;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public FeedbackFourOptionsModel getFeedbackFourOptionsModel() {
        return feedbackFourOptionsModel;
    }

    public void setFeedbackFourOptionsModel(FeedbackFourOptionsModel feedbackFourOptionsModel) {
        this.feedbackFourOptionsModel = feedbackFourOptionsModel;
    }

    public FeedbackTwoOptionsModel getFeedbackTwoOptionsModel() {
        return feedbackTwoOptionsModel;
    }

    public void setFeedbackTwoOptionsModel(FeedbackTwoOptionsModel feedbackTwoOptionsModel) {
        this.feedbackTwoOptionsModel = feedbackTwoOptionsModel;
    }

    public FeedbackInoutModel getFeedbackInoutModel() {
        return feedbackInoutModel;
    }

    public void setFeedbackInoutModel(FeedbackInoutModel feedbackInoutModel) {
        this.feedbackInoutModel = feedbackInoutModel;
    }
}
