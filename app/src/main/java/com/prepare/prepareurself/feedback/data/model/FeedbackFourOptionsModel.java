package com.prepare.prepareurself.feedback.data.model;

import java.util.List;

public class FeedbackFourOptionsModel {

    private int id;
    private String question;
    private String response;
    private List<String> options;

    public FeedbackFourOptionsModel(){

    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
