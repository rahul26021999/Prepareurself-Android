package com.prepare.prepareurself.courses.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateCourseRepository {
    ApiInterface apiInterface;;
    public RateCourseRepository(){
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
    }

}
