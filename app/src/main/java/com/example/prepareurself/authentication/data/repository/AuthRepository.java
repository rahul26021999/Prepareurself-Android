package com.example.prepareurself.authentication.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prepareurself.Apiservice.ApiClient;
import com.example.prepareurself.Apiservice.ApiInterface;
import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.example.prepareurself.authentication.data.db.repository.UserDBRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiInterface apiInterface;
    private UserDBRepository userDBRepository;

    public AuthRepository(Application application){
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        userDBRepository = new UserDBRepository(application);
    }

    public LiveData<AuthenticationResponseModel> login(String email, String password){

        final MutableLiveData<AuthenticationResponseModel> data = new MutableLiveData<>();

        apiInterface.loginUser(email,password).enqueue(new Callback<AuthenticationResponseModel>() {
            @Override
            public void onResponse(Call<AuthenticationResponseModel> call, Response<AuthenticationResponseModel> response) {
                AuthenticationResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code() == 0){
                        data.setValue(responseModel);
                        userDBRepository.clearUser();
                        userDBRepository.insertUser(responseModel.getUser_data());
                    }else{
                        data.setValue(responseModel);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<AuthenticationResponseModel> register(String firstName, String lastName, String email, String password){

        final MutableLiveData<AuthenticationResponseModel> data = new MutableLiveData<>();

        apiInterface.registerUser(firstName, lastName, password, email).enqueue(new Callback<AuthenticationResponseModel>() {
            @Override
            public void onResponse(Call<AuthenticationResponseModel> call, Response<AuthenticationResponseModel> response) {
                AuthenticationResponseModel responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getError_code() == 0){
                        data.setValue(responseModel);
                        userDBRepository.clearUser();
                        userDBRepository.insertUser(responseModel.getUser_data());
                    }else{
                        data.setValue(responseModel);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
