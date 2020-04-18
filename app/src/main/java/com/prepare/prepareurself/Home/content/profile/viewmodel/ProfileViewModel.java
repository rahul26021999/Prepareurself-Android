package com.prepare.prepareurself.Home.content.profile.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.Home.content.profile.data.repository.ProfileRepository;
import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<UserModel> userModelLiveData;
    private UserDBRepository userDBRepository;
    private ProfileRepository profileRepository;

    private MutableLiveData<List<PreferredTechStack>> listLiveData = new MutableLiveData<>();
    public MutableLiveData<List<PreferredTechStack>> listLiveDataEditable = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository();
        userDBRepository = new UserDBRepository(application);
        userModelLiveData = userDBRepository.getUserInfo();
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return userModelLiveData;
    }

    public void updateUser(String token, String firstName, String lastName, String dob, String phoneNumber){
        profileRepository.updateUser(token,firstName,lastName,dob,phoneNumber);
    }

    public void updatePrefernces(String token, List<Integer> integers){
        profileRepository.updatePreferences(token,integers);
    }

    public void addStacks(PreferredTechStack preferredTechStack){

        boolean isPresent = false;
        if (listLiveDataEditable.getValue()!=null){
            Log.d("chip_debug","main list course "+listLiveDataEditable.getValue().get(0).getCourse_name());
            for (PreferredTechStack p : listLiveDataEditable.getValue()){
                Log.d("chip_debug","is check " +p.getId() + " " + preferredTechStack.getId());
                if (p.getId() == preferredTechStack.getId()){
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent){
                Log.d("chip_debug","this is false");
                List<PreferredTechStack> tempList = new ArrayList<>(listLiveDataEditable.getValue());
                tempList.add(preferredTechStack);
                Log.d("chip_debug","new edit course "+preferredTechStack);
                Log.d("chip_debug","total edit course "+tempList.size());
//                listLiveDataEditable.getValue().add(preferredTechStack);
                listLiveDataEditable.getValue().clear();
                listLiveDataEditable.setValue(tempList);
            }

        }

    }

    public LiveData<List<PreferredTechStack>> getEditableStacks(){
        List<PreferredTechStack> preferredTechStacks = new ArrayList<>();
        PreferredTechStack p1 = new PreferredTechStack();
        p1.setId(1);
        p1.setCourse_name("Android");

        preferredTechStacks.add(p1);

        listLiveDataEditable.setValue(preferredTechStacks);

        return  listLiveDataEditable;

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
