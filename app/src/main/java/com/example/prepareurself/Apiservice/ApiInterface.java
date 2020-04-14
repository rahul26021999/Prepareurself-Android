package com.example.prepareurself.Apiservice;

import com.example.prepareurself.Home.content.dashboard.data.model.GetCourseResponseModel;
import com.example.prepareurself.authentication.data.model.AuthenticationResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    Call<AuthenticationResponseModel> registerUser(@Query("first_name") String firstName,
                                                   @Query("last_name") String lastName,
                                                   @Query("password") String password,
                                                   @Query("email") String email);

    @POST("login")
    Call<AuthenticationResponseModel> loginUser(@Query("email")String email,
                                                @Query("password")String password);

    @POST("get-courses")
    Call<GetCourseResponseModel> getCourses(@Query("token") String token);


}
