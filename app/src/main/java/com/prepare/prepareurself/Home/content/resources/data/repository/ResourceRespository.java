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
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourcesResponse;

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

    public LiveData<ResourcesResponse> getResourcesByID(String token, int topicId, int pageNumber, int count, String resourceType){

        final MutableLiveData<ResourcesResponse> data = new MutableLiveData<>();

        apiInterface.getResources(token,topicId, pageNumber, count, resourceType).enqueue(new Callback<GetResourcesResponse>() {
            @Override
            public void onResponse(Call<GetResourcesResponse> call, Response<GetResourcesResponse> response) {
                GetResourcesResponse resourcesResponse = response.body();
                if (resourcesResponse!=null){
                    if (resourcesResponse.getError_code()==0){
                        for (ResourceModel resourceModel : resourcesResponse.getResources().getData()) {
                            resourcesDbRepository.insertResource(resourceModel);
                        }
                        data.setValue(resourcesResponse.getResources());
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

    public void resourceViewed(String token, int resourceId){
        apiInterface.resourceViewed(token, resourceId).enqueue(new Callback<ResourceViewsResponse>() {
            @Override
            public void onResponse(Call<ResourceViewsResponse> call, Response<ResourceViewsResponse> response) {
                ResourceViewsResponse resourceViewsResponse = response.body();
                if (resourceViewsResponse!=null){
                    Log.d("resource_viewed",resourceViewsResponse.getMsg());
                }else{
                    Log.d("resource_viewed","null response");
                }
            }

            @Override
            public void onFailure(Call<ResourceViewsResponse> call, Throwable t) {
                Log.d("resource_viewed","failure "+ t.getLocalizedMessage());
            }
        });
    }

}
