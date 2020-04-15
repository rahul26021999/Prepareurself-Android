package com.prepare.prepareurself.Home.content.resources.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.Home.content.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.Home.content.resources.data.model.GetResourcesResponse;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResourceRespository {

    private ApiInterface apiInterface;
    private ResourcesDbRepository resourcesDbRepository;

    public ResourceRespository(Application application) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        resourcesDbRepository = new ResourcesDbRepository(application);
    }

    public LiveData<List<ResourceModel>> getResourcesByID(String token, int topicId){

        final MutableLiveData<List<ResourceModel>> data = new MutableLiveData<>();

        Log.d("debug","this is a");
        apiInterface.getResources(token,topicId).enqueue(new Callback<GetResourcesResponse>() {
            @Override
            public void onResponse(Call<GetResourcesResponse> call, Response<GetResourcesResponse> response) {
                GetResourcesResponse resourcesResponse = response.body();
                if (resourcesResponse!=null){
                    if (resourcesResponse.getError_code()==0){
                        resourcesDbRepository.deleteAllResources();
                        for (ResourceModel resourceModel : resourcesResponse.getResources().getData()) {
                            resourcesDbRepository.insertResource(resourceModel);
                        }
                        data.setValue(resourcesResponse.getResources().getData());
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetResourcesResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }

}
