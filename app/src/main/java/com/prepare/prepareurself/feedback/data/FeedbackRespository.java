package com.prepare.prepareurself.feedback.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.feedback.data.model.FeedbacksubmitModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackRespository {

    ApiInterface apiInterface;

    public FeedbackRespository() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public LiveData<FeedbacksubmitModel> storeFeedback(String token, List<String> answers){
        final MutableLiveData<FeedbacksubmitModel> data = new MutableLiveData<>();


        apiInterface.saveFeedback(token, answers).enqueue(new Callback<FeedbacksubmitModel>() {
            @Override
            public void onResponse(Call<FeedbacksubmitModel> call, Response<FeedbacksubmitModel> response) {
                FeedbacksubmitModel responseModel = response.body();
                if (responseModel!=null){
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<FeedbacksubmitModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }
}
