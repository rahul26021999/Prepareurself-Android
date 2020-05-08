package com.prepare.prepareurself.favourites.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.favourites.data.db.repository.LikedProjectRepository;
import com.prepare.prepareurself.favourites.data.db.repository.LikedResourceRespository;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesRepository {

    ApiInterface apiInterface;
    LikedProjectRepository likedProjectRepository;
    LikedResourceRespository likedResourceRespository;

    public FavouritesRepository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        likedProjectRepository = new LikedProjectRepository(application);
        likedResourceRespository = new LikedResourceRespository(application);
    }

    public LiveData<FavouritesResponseModel> fetchFavourites(String token, final String type, int count, final int page){

        final MutableLiveData<FavouritesResponseModel> data = new MutableLiveData<>();

        Log.d("fav", "fetchFavourites: "+apiInterface.fetchFavourites(token, type, count, page).request().url().toString());

        apiInterface.fetchFavourites(token, type, count, page).enqueue(new Callback<FavouritesResponseModel>() {
            @Override
            public void onResponse(Call<FavouritesResponseModel> call, Response<FavouritesResponseModel> response) {
                FavouritesResponseModel responseModel = response.body();
                if (responseModel!=null && responseModel.getError_code() == 0){

                    if (page == 1){

                        if (type.equals("project")){
                            likedProjectRepository.deleteAllProjects();
                        }else if (type.equals("resource")){
                            likedResourceRespository.deleteAllResources();
                        }
                    }

                    if (responseModel.getProjects()!=null){
                        for (LikedProjectsModel projectsModel : responseModel.getProjects().getData()){
                            projectsModel.setLike(1);
                            likedProjectRepository.insertProject(projectsModel);
                        }
                    }else if (responseModel.getResources()!=null){
                        for (LikedResourcesModel resourceModel : responseModel.getResources().getData()){
                            resourceModel.setLike(1);
                            likedResourceRespository.insertResource(resourceModel);
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
