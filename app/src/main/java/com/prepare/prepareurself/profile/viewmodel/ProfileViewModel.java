package com.prepare.prepareurself.profile.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.profile.data.db.repository.MyPreferenceDbRepository;
import com.prepare.prepareurself.profile.data.db.repository.PreferncesDbRespoitory;
import com.prepare.prepareurself.profile.data.model.AllPreferencesResponseModel;
import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.profile.data.model.UpdatePasswordResponseModel;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.data.repository.ProfileRepository;
import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<UserModel> userModelLiveData;
    private UserDBRepository userDBRepository;
    private ProfileRepository profileRepository;
    private PreferncesDbRespoitory preferncesDbRespoitory;
    private MyPreferenceDbRepository myPreferenceDbRepository;

    private MutableLiveData<HashMap<String,PreferredTechStack>> listLiveData = new MutableLiveData<>();
    public MutableLiveData<List<PreferredTechStack>> listLiveDataEditable = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
        userDBRepository = new UserDBRepository(application);
        userModelLiveData = userDBRepository.getUserInfo();
        preferncesDbRespoitory = new PreferncesDbRespoitory(application);
        myPreferenceDbRepository = new MyPreferenceDbRepository(application);
    }

    public LiveData<UserModel> getUserModelLiveData() {
        return userModelLiveData;
    }

    public LiveData<UpdatePreferenceResponseModel> updateUser(String token, String firstName, String lastName, String dob, String phoneNumber){
        return profileRepository.updateUser(token,firstName,lastName,dob,phoneNumber);
    }

    public LiveData<UpdatePasswordResponseModel> updatePassword(String token, String oldPass, String newPass){
        return profileRepository.updatePassword(token, oldPass, newPass);
    }

    public LiveData<UpdatePreferenceResponseModel> updatePrefernces(String token, List<Integer> integers){
        return profileRepository.updatePreferences(token,integers);
    }

    public void addStacks(PreferredTechStack preferredTechStack){

        boolean isPresent = false;
        if (listLiveDataEditable.getValue()!=null){
            Log.d("chip_debug","main list course "+listLiveDataEditable.getValue().get(0).getName());
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

    public void getAllPreferences(String token){
        profileRepository.getAllPreferences(token);
    }

    public LiveData<List<PreferredTechStack>> getPreferencesFromDb(){
        return preferncesDbRespoitory.getAllPreferences();
    }

    public LiveData<List<PreferredTechStack>> getEditableStacks(){
        List<PreferredTechStack> preferredTechStacks = new ArrayList<>();
        PreferredTechStack p1 = new PreferredTechStack();
        p1.setId(1);
        p1.setName("Android");

        preferredTechStacks.add(p1);

        listLiveDataEditable.setValue(preferredTechStacks);

        return  listLiveDataEditable;

    }

    public void saveMyPreference(UserModel userModel){
            userDBRepository.insertUser(userModel);
    }

    public LiveData<List<MyPreferenceTechStack>> getMyPreferredStack(){
        return myPreferenceDbRepository.getAllPreferences();
    }

    public LiveData<HashMap<String,PreferredTechStack>> getPreferredTechStacks(){

        HashMap<String,PreferredTechStack> preferredTechStacks = new HashMap<>();

        PreferredTechStack p1 = new PreferredTechStack();
        PreferredTechStack p2 = new PreferredTechStack();
        PreferredTechStack p3 = new PreferredTechStack();

        p1.setId(1);
        p1.setName("Android");

        p2.setId(2);
        p2.setName("Php");

        p3.setId(3);
        p3.setName("Node");


        preferredTechStacks.put(p1.getName(),p1);
        preferredTechStacks.put(p3.getName(),p3);
        preferredTechStacks.put(p2.getName(),p2);


        listLiveData.setValue(preferredTechStacks);

        return listLiveData;
    }

    public LiveData<UserModel> getUserPrefernces() {
        return userDBRepository.getUserInfo();
    }

    public void saveMyPreferenceList(List<MyPreferenceTechStack> myPreferenceTechStacks) {
        for (MyPreferenceTechStack myPreferenceTechStack : myPreferenceTechStacks){
            myPreferenceDbRepository.insertPreference(myPreferenceTechStack);
        }
    }

    public LiveData<UpdatePreferenceResponseModel> uploadImage(String  token, MultipartBody.Part body) {
        return profileRepository.uploadImage(token,body);
    }
}
