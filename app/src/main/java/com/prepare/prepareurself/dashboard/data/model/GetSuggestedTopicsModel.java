package com.prepare.prepareurself.dashboard.data.model;

import com.prepare.prepareurself.courses.data.model.TopicsModel;

import java.util.List;

public class GetSuggestedTopicsModel {

    private boolean success;
    private String course_id;
    private List<SuggestedTopicsModel> topics;

    public GetSuggestedTopicsModel(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public List<SuggestedTopicsModel> getTopics() {
        return topics;
    }

    public void setTopics(List<SuggestedTopicsModel> topics) {
        this.topics = topics;
    }
}
