package com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.favourites.data.db.repository.LikedResourceRespository;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.model.VideoShareResponseModel;
import com.prepare.prepareurself.resources.data.repository.ResourceRespository;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private PlaylistVideosDbRepository playlistVideosDbRepository;
    private ResourceRespository resourceRespository;
    private ResourcesDbRepository resourcesDbRepository;
    private LikedResourceRespository likedResourceRespository;

    private LiveData<List<VideoItemWrapper>> listLiveData = new MutableLiveData<>();

    public VideoViewModel(@NonNull Application application) {
        super(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
        resourceRespository = new ResourceRespository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
        likedResourceRespository = new LikedResourceRespository(application);
    }

    public LiveData<List<VideoItemWrapper>> getListLiveData(String playlistId) {

        listLiveData = playlistVideosDbRepository.getVideoItemWrapperByPlaylistId(playlistId);

        return listLiveData;
    }

    public LiveData<List<ResourceModel>> getResourceExceptId(int resourceId, String type, int topicId){
        return resourcesDbRepository.getResourceResourcesExcept(topicId,type,resourceId);
    }

    public LiveData<VideoShareResponseModel> getResourceFromRemote(String token, int resourceId) {
       return resourceRespository.getResourceByIdForShare(token,resourceId);
    }

    public void fetchResourceById(String token, int resourceId){
        resourceRespository.getResourceByIdForShare(token, resourceId);
    }

    public LiveData<ResourceModel> getResourceById(int resourceId, String type){
        return resourcesDbRepository.getResourceByResourceId(resourceId, type);
    }

    public LiveData<List<LikedResourcesModel>> getLikedResourceExceptId(int resourceId, String type){
        return likedResourceRespository.getLikedResourcesExceptId(resourceId, type);
    }

    public LiveData<ResourceViewsResponse> resourceViewed(String token, int resourceId){
        return resourceRespository.resourceViewed(token,resourceId);
    }
    public void saveResource(ResourceModel videoResources) {
        resourcesDbRepository.insertResource(videoResources);
    }

}
