package com.prepare.prepareurself.favourites.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.courses.data.repository.ProjectsRespository;
import com.prepare.prepareurself.favourites.data.db.repository.LikedProjectRepository;
import com.prepare.prepareurself.favourites.data.db.repository.LikedResourceRespository;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.favourites.data.repository.FavouritesRepository;
import com.prepare.prepareurself.resources.data.repository.ResourceRespository;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private LiveData<FavouritesResponseModel> favouritesResponseModelLiveData = new MutableLiveData<>();
    private FavouritesRepository favouritesRepository;
    private LikedProjectRepository likedProjectRepository;
    private LikedResourceRespository likedResourceRespository;
    private LiveData<List<LikedProjectsModel>> likedProjectsModelLiveData;
    private LiveData<List<LikedResourcesModel>> likedResourcesModelLiveData;
    private ResourceRespository resourceRespository;
    private ProjectsRespository projectsRespository;

    public FavouritesViewModel(Application application){
        super(application);
        favouritesRepository = new FavouritesRepository(application);
        likedProjectRepository = new LikedProjectRepository(application);
        likedResourceRespository = new LikedResourceRespository(application);
        resourceRespository = new ResourceRespository(application);
        projectsRespository = new ProjectsRespository(application);
    }

    public LiveData<FavouritesResponseModel> getFavouritesResponseModelLiveData() {
        return favouritesResponseModelLiveData;
    }

    public LiveData<FavouritesResponseModel> fetchFavourites(){
        return favouritesResponseModelLiveData;
    }

    public void getFavourites(String token, String type, int count, int page){
        favouritesResponseModelLiveData = favouritesRepository.fetchFavourites(token,type, count, page);
    }

    public LiveData<List<LikedResourcesModel>> getLikedResourcesModelLiveData() {
        return likedResourceRespository.getAllResources();
    }

    public LiveData<List<LikedProjectsModel>> getLikedProjectsModelLiveData() {
        return likedProjectRepository.getAllProjects();
    }

    public void likeProject(String token, int projectId, int like){
        projectsRespository.likeProject(token, projectId, like);
    }

    public void likeResource(String token, int resourceId, int like){
        resourceRespository.resourceLiked(token, resourceId, like);
    }

}
