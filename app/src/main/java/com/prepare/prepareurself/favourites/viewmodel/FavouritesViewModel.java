package com.prepare.prepareurself.favourites.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
import com.prepare.prepareurself.favourites.data.repository.FavouritesRepository;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private LiveData<FavouritesResponseModel> favouritesResponseModelLiveData = new MutableLiveData<>();
    private LiveData<List<ProjectsModel>> projectsLiveData = new MutableLiveData<>();
    private LiveData<List<ResourceModel>> resourceLiveData = new MutableLiveData<>();
    private FavouritesRepository favouritesRepository;

    public FavouritesViewModel(Application application){
        super(application);
        favouritesRepository = new FavouritesRepository(application);
    }

    public LiveData<FavouritesResponseModel> getFavouritesResponseModelLiveData() {
        return favouritesResponseModelLiveData;
    }

    public LiveData<List<ProjectsModel>> getProjectsLiveData() {
        return projectsLiveData;
    }

    public LiveData<List<ResourceModel>> getResourceLiveData() {
        return resourceLiveData;
    }

    public void fetchFavourites(String token, String type,int count, int page){
        favouritesResponseModelLiveData = favouritesRepository.fetchFavourites(token,type, count, page);
    }
}
