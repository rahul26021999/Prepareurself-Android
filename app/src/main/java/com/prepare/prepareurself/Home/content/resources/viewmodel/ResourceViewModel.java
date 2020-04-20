package com.prepare.prepareurself.Home.content.resources.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.Home.content.resources.data.repository.ResourceRespository;

import java.util.List;

public class ResourceViewModel extends AndroidViewModel {

    private LiveData<List<ResourceModel>> listLiveData = new MutableLiveData<>();
    private LiveData<ResourcesResponse> responseLiveData = new MutableLiveData<>();

    private ResourceRespository resourceRespository;
    private ResourcesDbRepository resourcesDbRepository;

    public ResourceViewModel(@NonNull Application application) {
        super(application);
        resourceRespository = new ResourceRespository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
    }

    public void resourceViewed(String token, int resourceId){
        resourceRespository.resourceViewed(token,resourceId);
    }
    // create like method
    public  void  resourcesLiked(String token, int resourceId, int like){
        resourceRespository.resourceLiked(token,resourceId,like);
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
}
