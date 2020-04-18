package com.prepare.prepareurself.Home.content.profile.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<UserModel> userModelLiveData;
    private UserDBRepository userDBRepository;

    private MutableLiveData<List<PreferredTechStack>> listLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userDBRepository = new UserDBRepository(application);
        userModelLiveData = userDBRepository.getUserInfo();
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return userModelLiveData;
    }

    public void updatePreferredStackList(List<PreferredTechStack> preferredTechStacks){
        listLiveData.setValue(preferredTechStacks);
    }

    public LiveData<List<PreferredTechStack>> getPreferredTechStacks(){

        List<PreferredTechStack> preferredTechStacks = new ArrayList<>();

        PreferredTechStack p1 = new PreferredTechStack();
        PreferredTechStack p2 = new PreferredTechStack();
        PreferredTechStack p3 = new PreferredTechStack();

        p1.setId(1);
        p1.setCourse_name("Android");

        p2.setId(2);
        p2.setCourse_name("Php");

        p3.setId(3);
        p3.setCourse_name("Node");

        preferredTechStacks.add(p1);
        preferredTechStacks.add(p2);
        preferredTechStacks.add(p3);

        listLiveData.setValue(preferredTechStacks);

        return listLiveData;
    }

}
