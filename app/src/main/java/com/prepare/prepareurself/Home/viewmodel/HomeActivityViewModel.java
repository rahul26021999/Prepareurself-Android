package com.prepare.prepareurself.Home.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.UserModel;

public class HomeActivityViewModel extends AndroidViewModel {

    UserDBRepository dbRepository;
    private LiveData<UserModel> userModelLiveData = new MutableLiveData<>();

    public HomeActivityViewModel(Application application){
        super(application);
        dbRepository = new UserDBRepository(application);
    }

    public void retrieveUserData(){
        userModelLiveData = dbRepository.getUserInfo();
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return userModelLiveData;
    }
}
