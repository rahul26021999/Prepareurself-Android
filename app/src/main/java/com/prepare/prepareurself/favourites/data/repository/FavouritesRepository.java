package com.prepare.prepareurself.favourites.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.Api;
import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
import com.prepare.prepareurself.favourites.data.model.LikedItemDataModel;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesRepository {

    ApiInterface apiInterface;
    ProjectsDbRepository projectsDbRepository;
    ResourcesDbRepository resourcesDbRepository;

    public FavouritesRepository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        projectsDbRepository = new ProjectsDbRepository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
    }

    public LiveData<FavouritesResponseModel> fetchFavourites(String token, int count, int page){

        final MutableLiveData<FavouritesResponseModel> data = new MutableLiveData<>();

        apiInterface.fetchFavourites(token, count, page).enqueue(new Callback<FavouritesResponseModel>() {
            @Override
            public void onResponse(Call<FavouritesResponseModel> call, Response<FavouritesResponseModel> response) {
                FavouritesResponseModel responseModel = response.body();
                if (responseModel!=null && responseModel.getError_code() == 0){
                    for (LikedItemDataModel likedItemDataModel : responseModel.getLikedItems().getData()){
                        if (likedItemDataModel.getProject()!=null){
                            projectsDbRepository.insertProject(likedItemDataModel.getProject());
                        }
                        if (likedItemDataModel.getResource()!=null){
                            resourcesDbRepository.insertResource(likedItemDataModel.getResource());
                        }
                    }
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<FavouritesResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
