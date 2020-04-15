package com.prepare.prepareurself.Home.content.courses.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.Home.content.courses.data.db.repository.TopicsDbRepository;
import com.prepare.prepareurself.Home.content.courses.data.model.GetTopicResponseModel;
import com.prepare.prepareurself.Home.content.courses.data.model.TopicsModel;

import java.util.List;

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

    public LiveData<List<TopicsModel>> getTopicsByIId(String token, int courseId){
        final MutableLiveData<List<TopicsModel>> data = new MutableLiveData<>();

        apiInterface.getTopics(token,courseId).enqueue(new Callback<GetTopicResponseModel>() {
            @Override
            public void onResponse(Call<GetTopicResponseModel> call, Response<GetTopicResponseModel> response) {
                GetTopicResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code() == 0){
                        topicsDbRepository.deleteAllTopics();
                        for (TopicsModel topicsModel : responseModel.getTopics().getData()) {
                            topicsDbRepository.insertTopic(topicsModel);
                        }
                        data.setValue(responseModel.getTopics().getData());
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetTopicResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }

}
