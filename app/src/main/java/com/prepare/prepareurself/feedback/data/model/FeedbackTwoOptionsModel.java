package com.prepare.prepareurself.feedback.data.model;

import java.util.List;

public class FeedbackTwoOptionsModel {

    private int id;
    private String question;
    private String answer;
    private List<String> options;


    public FeedbackTwoOptionsModel(){

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
