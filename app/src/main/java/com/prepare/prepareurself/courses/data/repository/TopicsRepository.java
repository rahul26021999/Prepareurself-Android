package com.prepare.prepareurself.courses.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.courses.data.db.repository.TopicsDbRepository;
import com.prepare.prepareurself.courses.data.model.GetTopicResponseModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicsRepository {

    private ApiInterface apiInterface;
    private TopicsDbRepository topicsDbRepository;

    public TopicsRepository(Application application) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        topicsDbRepository = new TopicsDbRepository(application);
    }

    public LiveData<TopicsResponseModel> getTopicsByIId(String token, int courseId, int count, final int pageNumber){
        final MutableLiveData<TopicsResponseModel> topicResponse = new MutableLiveData<>();

        Log.d("url_debug",apiInterface.getTopics(token,courseId,count,pageNumber).request().url().toString());
        apiInterface.getTopics(token,courseId, count, pageNumber).enqueue(new Callback<GetTopicResponseModel>() {
            @Override
            public void onResponse(Call<GetTopicResponseModel> call, Response<GetTopicResponseModel> response) {
                GetTopicResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code() == 0){
                       // topicsDbRepository.deleteAllTopics();
                        if (pageNumber == 1){
                            topicsDbRepository.deleteAllTopics();
                        }
                        for (TopicsModel topicsModel : responseModel.getTopics().getData()) {
                            topicsDbRepository.insertTopic(topicsModel);
                        }
                        topicResponse.setValue(responseModel.getTopics());
                    }else{
                        topicResponse.setValue(null);
                    }
                }else{
                    topicResponse.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetTopicResponseModel> call, Throwable t) {
                topicResponse.setValue(null);
            }
        });

        return topicResponse;

    }

}
