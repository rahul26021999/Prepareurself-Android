package com.prepare.prepareurself.resources.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.dashboard.data.db.repository.CourseDbRepository;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.favourites.data.db.repository.LikedResourceRespository;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.resources.data.repository.ResourceRespository;

import java.util.List;

public class ResourceViewModel extends AndroidViewModel {

    private LiveData<List<ResourceModel>> listLiveData = new MutableLiveData<>();
    private LiveData<ResourcesResponse> responseLiveData = new MutableLiveData<>();
    private LiveData<ResourceModel> resourceModelLiveData = new MutableLiveData<>();

    private ResourceRespository resourceRespository;
    private ResourcesDbRepository resourcesDbRepository;
    private CourseDbRepository courseDbRepository;
    private LikedResourceRespository likedResourceRespository;

    public ResourceViewModel(@NonNull Application application) {
        super(application);
        resourceRespository = new ResourceRespository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
        courseDbRepository = new CourseDbRepository(application);
        likedResourceRespository = new LikedResourceRespository(application);
    }

    public LiveData<ResourceViewsResponse> resourceViewed(String token, int resourceId){
        return resourceRespository.resourceViewed(token,resourceId);
    }
    // create like method
    public  LiveData<ResourceLikesResponse>  resourcesLiked(String token, int resourceId, int like){
        return resourceRespository.resourceLiked(token,resourceId,like);
    }

    public void fetchResources(String token, int topicId, int pageNumber, int count, String type){
        responseLiveData = resourceRespository.getResourcesByID(token,topicId, pageNumber, count, type);
    }

    public LiveData<ResourcesResponse> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<List<ResourceModel>> getListLiveData(int topicId, String type) {

        if (listLiveData.getValue() == null){
            listLiveData = resourcesDbRepository.getResourcesByID(topicId, type);
        }

        return listLiveData;
    }

    public void saveResource(ResourceModel videoResources) {
        resourcesDbRepository.insertResource(videoResources);
    }

    public LiveData<CourseModel> getCourseById(int courseId) {
        return courseDbRepository.getCourseById(courseId);
    }

    public void insertLikedResource(LikedResourcesModel likedResourcesModel){
        likedResourceRespository.insertResource(likedResourcesModel);
    }

    public void deleteLikedResource(LikedResourcesModel likedResourcesModel){
        likedResourceRespository.deleteResource(likedResourcesModel);
    }
}
