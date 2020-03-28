package com.example.prepareurself.quiz.model;

import java.util.HashMap;

public class Question {

    public String questionNo, questionText;
    public HashMap<Integer,String> options;
    public Integer correctOption;

    public Question(String questionNo, String questionText, Integer correctOption, HashMap<Integer,String> options) {
        this.questionNo = questionNo;
        this.questionText = questionText;
        this.correctOption = correctOption;
        this.options = options;
    }
}
