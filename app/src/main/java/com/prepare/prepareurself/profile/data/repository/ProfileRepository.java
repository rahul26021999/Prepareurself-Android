package com.prepare.prepareurself.profile.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.profile.data.db.repository.PreferncesDbRespoitory;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.data.model.AllPreferencesResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private ApiInterface apiInterface;
    private UserDBRepository userDBRepository;
    private PreferncesDbRespoitory preferncesDbRespoitory;

    public ProfileRepository(Application application){
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        userDBRepository = new UserDBRepository(application);
        preferncesDbRespoitory = new PreferncesDbRespoitory(application);
    }

    public LiveData<UpdatePreferenceResponseModel> updateUser(String token, String firstName, String lastName, String dob, String phoneNumber){

        final MutableLiveData<UpdatePreferenceResponseModel> data = new MutableLiveData<>();

        apiInterface.updateUser(token, firstName, lastName,dob , phoneNumber).enqueue(new Callback<UpdatePreferenceResponseModel>() {
            @Override
            public void onResponse(Call<UpdatePreferenceResponseModel> call, Response<UpdatePreferenceResponseModel> response) {

                UpdatePreferenceResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code() == 0){
                        userDBRepository.insertUser(responseModel.getUser_data());
                    }
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdatePreferenceResponseModel> call, Throwable t) {
                Log.d("update_user_response", "failure" +t.getLocalizedMessage()+"");
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<UpdatePreferenceResponseModel> updatePreferences(String token, List<Integer> integers) {

        final MutableLiveData<UpdatePreferenceResponseModel> data = new MutableLiveData<>();

        Log.d("update_prefernce_respon",apiInterface.updatePreference(token,integers).request().url().toString());
        apiInterface.updatePreference(token,integers).enqueue(new Callback<UpdatePreferenceResponseModel>() {
            @Override
            public void onResponse(Call<UpdatePreferenceResponseModel> call, Response<UpdatePreferenceResponseModel> response) {
                UpdatePreferenceResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code()==0){
                        userDBRepository.clearUser();
                        userDBRepository.insertUser(responseModel.getUser_data());
                    }
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdatePreferenceResponseModel> call, Throwable t) {
                Log.d("update_prefernce_respon",t.getLocalizedMessage()+"");
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<AllPreferencesResponseModel> getAllPreferences(String token){

        final MutableLiveData<AllPreferencesResponseModel> data = new MutableLiveData<>();

        apiInterface.getAllPreferences(token).enqueue(new Callback<AllPreferencesResponseModel>() {
            @Override
            public void onResponse(Call<AllPreferencesResponseModel> call, Response<AllPreferencesResponseModel> response) {
                AllPreferencesResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code() == 0){
                        for (PreferredTechStack preferredTechStack : responseModel.getPreferences()){
                            preferncesDbRespoitory.insertPreference(preferredTechStack);
                        }
                        data.setValue(responseModel);
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AllPreferencesResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}


