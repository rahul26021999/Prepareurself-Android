package com.example.prepareurself.authentication.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prepareurself.Apiservice.ApiClient;
import com.example.prepareurself.Apiservice.ApiInterface;
import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.example.prepareurself.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiInterface apiInterface;
    private static AuthRepository authRepository;

    private AuthRepository(){
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
    }

    public synchronized static AuthRepository getInstance(){
        if (authRepository == null){
            authRepository = new AuthRepository();
        }

        return authRepository;
    }

    public LiveData<AuthenticationResponseModel> login(String email, String password){

        final MutableLiveData<AuthenticationResponseModel> data = new MutableLiveData<>();

        apiInterface.loginUser(email,password).enqueue(new Callback<AuthenticationResponseModel>() {
            @Override
            public void onResponse(Call<AuthenticationResponseModel> call, Response<AuthenticationResponseModel> response) {
                AuthenticationResponseModel responseModel = response.body();
                data.setValue(responseModel);
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
                data.setValue(responseModel);
            }

            @Override
            public void onFailure(Call<AuthenticationResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
