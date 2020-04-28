package com.prepare.prepareurself.feedback.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.prepare.prepareurself.feedback.data.FeedbackRespository;
import com.prepare.prepareurself.feedback.data.model.FeedbackFourOptionsModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackInoutModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackParentModel;
import com.prepare.prepareurself.feedback.data.model.FeedbackTwoOptionsModel;
import com.prepare.prepareurself.feedback.data.model.FeedbacksubmitModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackViewModel extends ViewModel {

    private FeedbackRespository feedbackRespository;

    public FeedbackViewModel() {
        feedbackRespository = new FeedbackRespository();
    }

    public LiveData<FeedbacksubmitModel> saveFeedback(String token, List<String> answers){

        return feedbackRespository.storeFeedback(token,answers);

    }

    public List<FeedbackParentModel> getFeedbackParent(){

        List<FeedbackParentModel> feedbackParentModels = new ArrayList<>();

        // wait abhi.. api theek kr rha h gupta.....

        FeedbackFourOptionsModel f1 = new FeedbackFourOptionsModel();
        f1.setId(1);
        f1.setQuestion("Did you like our design/user interface of prepareurself?");
        f1.setResponse("");
        List<String> options1 = new ArrayList<>();
        options1.add("Great");
        options1.add("Good");
        options1.add("Fair");
        options1.add("Bad");
        f1.setOptions(options1);
        FeedbackParentModel feedbackParentModel1 = new FeedbackParentModel(1,f1);

        FeedbackTwoOptionsModel f2 = new FeedbackTwoOptionsModel();
        f2.setId(2);
        f2.setQuestion("Did we have the selection(variety offered) you were looking for?");
        f2.setAnswer("");
        List<String> options3 = new ArrayList<>();
        options3.add("YES");
        options3.add("No");
        f2.setOptions(options3);
        FeedbackParentModel feedbackParentModel2 = new FeedbackParentModel(2,f2);

        FeedbackTwoOptionsModel f3 = new FeedbackTwoOptionsModel();
        f3.setId(3);
        f3.setQuestion("Was it easy to find what you were looking for?");
        f3.setAnswer("");
        List<String> options4 = new ArrayList<>();
        options4.add("YES");
        options4.add("No");
        f3.setOptions(options4);
        FeedbackParentModel feedbackParentModel3 = new FeedbackParentModel(2,f3);

        FeedbackFourOptionsModel f4 = new FeedbackFourOptionsModel();
        f4.setId(4);
        f4.setQuestion("How useful are the tech stack  resources?");
        f4.setResponse("");
        List<String> options2 = new ArrayList<>();
        options2.add("Great");
        options2.add("Good");
        options2.add("Fair");
        options2.add("Bad");
        f4.setOptions(options2);
        FeedbackParentModel feedbackParentModel4 = new FeedbackParentModel(1,f4);

        FeedbackTwoOptionsModel f5 = new FeedbackTwoOptionsModel();
        f5.setId(5);
        f5.setQuestion("Was it easy to find what you were looking for?");
        f5.setAnswer("");
        List<String> options5 = new ArrayList<>();
        options5.add("YES");
        options5.add("No");
        f5.setOptions(options5);
        FeedbackParentModel feedbackParentModel5 = new FeedbackParentModel(2,f5);

        FeedbackInoutModel f6 = new FeedbackInoutModel();
        f6.setId(6);
        f6.setQuestion("Would you like to give any suggestions?");
        f6.setAnswer("");

        FeedbackParentModel feedbackParentModel6 = new FeedbackParentModel(3,f6);

        feedbackParentModels.add(feedbackParentModel1);
        feedbackParentModels.add(feedbackParentModel2);
        feedbackParentModels.add(feedbackParentModel3);
        feedbackParentModels.add(feedbackParentModel4);
        feedbackParentModels.add(feedbackParentModel5);
        feedbackParentModels.add(feedbackParentModel6);

        return feedbackParentModels;

    }

}
