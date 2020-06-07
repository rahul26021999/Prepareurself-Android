package com.prepare.prepareurself.courses.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailRepository {

    ApiInterface apiInterface;

    public CourseDetailRepository(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public LiveData<CourseDetailReponseModel> fetchCourseDetails(String token, int courseId){

        final MutableLiveData<CourseDetailReponseModel> data = new MutableLiveData<>();

        apiInterface.fetchCourseDetails(token, courseId).enqueue(new Callback<CourseDetailReponseModel>() {
            @Override
            public void onResponse(Call<CourseDetailReponseModel> call, Response<CourseDetailReponseModel> response) {
                CourseDetailReponseModel res = response.body();

                if (res!=null && res.getError_code() == 0){
                    data.setValue(res);
                }else{
                    data.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<CourseDetailReponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }

}
