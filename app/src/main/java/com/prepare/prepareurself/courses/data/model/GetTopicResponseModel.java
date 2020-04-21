package com.prepare.prepareurself.courses.data.model;

public class GetTopicResponseModel {

    private int error_code;
    TopicsResponseModel topics;



    public GetTopicResponseModel() {
    }

    public TopicsResponseModel getTopics() {
        return topics;
    }

    public void setTopics(TopicsResponseModel topics) {
        this.topics = topics;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
