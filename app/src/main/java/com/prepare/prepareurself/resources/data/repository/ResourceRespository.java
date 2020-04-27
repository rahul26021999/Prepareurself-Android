package com.prepare.prepareurself.resources.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.GetResourcesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.resources.data.model.VideoShareResponseModel;

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

    public LiveData<ResourcesResponse> getResourcesByID(String token, int topicId, final int pageNumber, int count, String resourceType){

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
                    Log.d("resource_viewed",resourceViewsResponse.getMessage());
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

    //wrte here for likes
    public  void  resourceLiked(String token, int resource_id , int like){
        apiInterface.resourceLiked(token, resource_id, like).enqueue(new Callback<ResourceLikesResponse>() {
            @Override
            public void onResponse(Call<ResourceLikesResponse> call, Response<ResourceLikesResponse> response) {
                ResourceLikesResponse resourceLikesResponse = response.body();
                if (resourceLikesResponse!=null){
                    Log.d("resource_Liked",resourceLikesResponse.getMessage());
                }else{
                    Log.d("resource_Liked","null response");
                }
            }

            @Override
            public void onFailure(Call<ResourceLikesResponse> call, Throwable t) {
                Log.d("resource_Liked","failure "+ t.getLocalizedMessage());
            }
        });
    }

    public LiveData<VideoShareResponseModel> getResourceByIdForShare(String token, int resourceId) {
        final MutableLiveData<VideoShareResponseModel> data = new MutableLiveData<>();

        apiInterface.getResouceById(token, resourceId).enqueue(new Callback<VideoShareResponseModel>() {
            @Override
            public void onResponse(Call<VideoShareResponseModel> call, Response<VideoShareResponseModel> response) {
                VideoShareResponseModel responseModel = response.body();
                if (responseModel!=null){
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<VideoShareResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }
}
