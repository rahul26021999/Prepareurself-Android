package com.prepare.prepareurself.feedback.data.model;

public class FeedbackModel {

    private int id;
    private String question;
    private int response;

    public FeedbackModel(){

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

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }
}
