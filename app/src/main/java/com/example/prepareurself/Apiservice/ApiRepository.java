package com.example.prepareurself.Apiservice;

import android.util.Log;

import retrofit2.Callback;

public class ApiRepository {

    ApiInterface apiInterface=ApiClient.getApiClient().create(ApiInterface.class);

    public ApiRepository() {
    }

    public void registerUser(String firstName, String lastname, String username, String password, String email, Callback callback){
        apiInterface.registerUser(firstName,lastname,username,password,email).enqueue(callback);
//        Log.d("register_url",apiInterface.registerUser(firstName,lastname,username,password,email).request().url().toString());
    }

    public void checkUsername(String userName, Callback callback){
        apiInterface.checkUsername(userName).enqueue(callback);
    }

}
