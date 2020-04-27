package com.prepare.prepareurself.dashboard.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.dashboard.data.db.repository.SuggestedProjectsDbRespository;
import com.prepare.prepareurself.dashboard.data.db.repository.SuggestedTopicsDbRepository;
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedProjectsModel;
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRespoisitory {

    private ApiInterface apiInterface;
    private SuggestedProjectsDbRespository suggestedProjectsDbRespository;
    private SuggestedTopicsDbRepository suggestedTopicsDbRepository;

    public DashboardRespoisitory(Application application) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        suggestedProjectsDbRespository = new SuggestedProjectsDbRespository(application);
        suggestedTopicsDbRepository = new SuggestedTopicsDbRepository(application);

    }

    public LiveData<List<SuggestedTopicsModel>> fetSuggestedTopics(String token) {

        final MutableLiveData<List<SuggestedTopicsModel>> data = new MutableLiveData<>();

        apiInterface.getSuggestedTopics(token).enqueue(new Callback<GetSuggestedTopicsModel>() {
            @Override
            public void onResponse(Call<GetSuggestedTopicsModel> call, Response<GetSuggestedTopicsModel> response) {
                GetSuggestedTopicsModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.isSuccess()){
                        suggestedTopicsDbRepository.deleteAllTopics();
                        for (SuggestedTopicsModel suggestedTopicsModel : responseModel.getTopics()){
                            suggestedTopicsDbRepository.insertTopic(suggestedTopicsModel);
                        }
                        data.setValue(responseModel.getTopics());
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetSuggestedTopicsModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<SuggestedProjectModel>> fetchSuggestedProjects(String token){

        final MutableLiveData<List<SuggestedProjectModel>> data = new MutableLiveData<>();

        apiInterface.getSuggestedProjects(token).enqueue(new Callback<GetSuggestedProjectsModel>() {
            @Override
            public void onResponse(Call<GetSuggestedProjectsModel> call, Response<GetSuggestedProjectsModel> response) {
                GetSuggestedProjectsModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.isSuccess()){
                        suggestedProjectsDbRespository.deleteAllProjects();
                        for (SuggestedProjectModel suggestedProjectModel : responseModel.getProjects()){
                            suggestedProjectsDbRespository.insertProject(suggestedProjectModel);
                        }
                        data.setValue(responseModel.getProjects());
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetSuggestedProjectsModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }
}
