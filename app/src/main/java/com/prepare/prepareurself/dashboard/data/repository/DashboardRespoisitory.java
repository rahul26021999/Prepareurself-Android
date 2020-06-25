package com.prepare.prepareurself.dashboard.data.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.dashboard.data.db.repository.SuggestedProjectsDbRespository;
import com.prepare.prepareurself.dashboard.data.db.repository.SuggestedTopicsDbRepository;
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedProjectsModel;
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.data.model.HomepageData;
import com.prepare.prepareurself.dashboard.data.model.HomepageResponseModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

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

    public LiveData<HomepageResponseModel> fetchHomePageData(String token, final Context context){

        final MutableLiveData<HomepageResponseModel> data = new MutableLiveData<>();

        Log.d("home_data_debug", "fetchHomePageData: "+apiInterface.fetchHomePage(token).request().url().toString());

        apiInterface.fetchHomePage(token).enqueue(new Callback<HomepageResponseModel>() {
            @Override
            public void onResponse(Call<HomepageResponseModel> call, Response<HomepageResponseModel> response) {
                HomepageResponseModel responseModel  = response.body();
                Log.d("TAGRESPONSE",""+responseModel.getError_code());
                if (response.code() == 401){
                    Log.d("TAGRESPONSE",""+responseModel.getError_code());
                    PrefManager prefManager = new PrefManager(context);
                    Utility.showLongToast(context,"Session Expired! Please login again");
                    Intent intent = new Intent();
                    intent.setClass(context, AuthenticationActivity.class);
                    prefManager.saveBoolean(Constants.ISLOGGEDIN, false);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
                if (responseModel!=null){
                    Log.d("TAGRESPONSE",""+responseModel.getError_code());

                    if (responseModel.getError_code()==0){
                        data.setValue(responseModel);
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<HomepageResponseModel> call, Throwable t) {
                Log.d("dashboard_call_failure",t.getLocalizedMessage()+"");
                data.setValue(null);
            }
        });

        return data;

    }

}

