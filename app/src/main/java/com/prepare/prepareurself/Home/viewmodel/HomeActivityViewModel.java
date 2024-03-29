package com.prepare.prepareurself.Home.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.profile.data.repository.ProfileRepository;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.resources.data.repository.ResourceRespository;

public class HomeActivityViewModel extends AndroidViewModel {

    UserDBRepository dbRepository;
    private LiveData<UserModel> userModelLiveData = new MutableLiveData<>();
    private ResourceRespository resourceRespository;
    private ResourcesDbRepository resourcesDbRepository;
    private ProfileRepository profileRepository;
    public HomeActivityViewModel(Application application){
        super(application);
        dbRepository = new UserDBRepository(application);
        resourceRespository = new ResourceRespository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
        profileRepository = new ProfileRepository(application);
    }

    public void retrieveUserData(){
        userModelLiveData = dbRepository.getUserInfo();
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return userModelLiveData;
    }

    public LiveData<ResourceViewsResponse> resourceViewed(String token, int resourceId){
        return resourceRespository.resourceViewed(token,resourceId);
    }
    public void saveResource(ResourceModel videoResources) {
        resourcesDbRepository.insertResource(videoResources);
    }

    public void updateToken(String token, String androidToken){
        profileRepository.updateAndroidToken(token, androidToken);
    }

    public void getUser(String token, Context context){
        profileRepository.getUser(token, context);
    }

}
