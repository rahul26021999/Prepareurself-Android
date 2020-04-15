package com.prepare.prepareurself.Home.content.resources.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.Home.content.resources.data.repository.ResourceRespository;

import java.util.List;

public class ResourceViewModel extends AndroidViewModel {

    private LiveData<List<ResourceModel>> listLiveData = new MutableLiveData<>();

    private ResourceRespository resourceRespository;
    private ResourcesDbRepository resourcesDbRepository;

    public ResourceViewModel(@NonNull Application application) {
        super(application);
        resourceRespository = new ResourceRespository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
    }

    public void fetchResources(String token, int topicId){
        listLiveData = resourceRespository.getResourcesByID(token,topicId);
    }

    public LiveData<List<ResourceModel>> getListLiveData(int topicId, String type) {

        if (listLiveData.getValue() == null){
            listLiveData = resourcesDbRepository.getResourcesByID(topicId, type);
        }

        return listLiveData;
    }
}
