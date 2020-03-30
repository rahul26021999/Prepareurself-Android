package com.example.prepareurself.Apiservice;

import com.example.prepareurself.authentication.registration.model.CheckUsernameResponse;
import com.example.prepareurself.authentication.registration.model.RegisterResponseModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    Call<RegisterResponseModel> registerUser(@Query("first_name") String firstName,
                                             @Query("last_name") String lastName,
                                             @Query("username") String username,
                                             @Query("password") String password,
                                             @Query("email") String email);

    @POST("check-username")
    Call<CheckUsernameResponse> checkUsername(@Query("username") String userName);

}
