package com.prepare.prepareurself.feedback.ui;

import androidx.lifecycle.ViewModel;

import com.prepare.prepareurself.feedback.data.model.FeedbackModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackViewModel extends ViewModel {

    public List<FeedbackModel> getFeebacks(){
        List<FeedbackModel> feedbackModels = new ArrayList<>();

        FeedbackModel f1 = new FeedbackModel();
        FeedbackModel f2 = new FeedbackModel();
        FeedbackModel f3 = new FeedbackModel();

        f1.setId(1);
        f1.setQuestion("this is question 1");
        f1.setResponse(1);

        f2.setId(2);
        f2.setQuestion("this is question 2");
        f2.setResponse(1);

        f3.setId(3);
        f3.setQuestion("this is question 3");
        f3.setResponse(1);

        feedbackModels.add(f1);
        feedbackModels.add(f2);
        feedbackModels.add(f3);

        return feedbackModels;

    }

}
