package com.example.prepareurself.Home.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.example.prepareurself.authentication.data.model.UserModel;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<UserModel> userModelLiveData;
    private UserDBRepository userDBRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userDBRepository = new UserDBRepository(application);
        userModelLiveData = userDBRepository.getUserInfo();
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return userModelLiveData;
    }
}
