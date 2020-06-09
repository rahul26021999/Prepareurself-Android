package com.prepare.prepareurself.courses.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.courses.data.model.AddToUserPrefResponseModel;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailRepository {

    ApiInterface apiInterface;

    public CourseDetailRepository(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }
    //CourseDetailReponseModel
    public LiveData<CourseDetailReponseModel> fetchCourseDetails(String token, int courseId){

        final MutableLiveData<CourseDetailReponseModel> data = new MutableLiveData<>();

        apiInterface.fetchCourseDetails(token, courseId).enqueue(new Callback<CourseDetailReponseModel>() {
            @Override
            public void onResponse(Call<CourseDetailReponseModel> call, Response<CourseDetailReponseModel> response) {
                CourseDetailReponseModel res = response.body();
                Log.d("course_det",""+res+"code"+res.getError_code());

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

    //RateCourseResponseModel
    public LiveData<RateCourseResponseModel> fetchRateCourse(String token, int course_id, int rating){
        final  MutableLiveData<RateCourseResponseModel> data=new MutableLiveData<>();
        apiInterface.fetchRateCourseDetails(token,course_id,rating).enqueue(new Callback<RateCourseResponseModel>() {
            @Override
            public void onResponse(Call<RateCourseResponseModel> call, Response<RateCourseResponseModel> response) {
                RateCourseResponseModel res=response.body();
                if (res!=null && res.getError_code() == 0){
                    data.setValue(res);
                }else{
                    data.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<RateCourseResponseModel> call, Throwable t) {
                data.setValue(null);
                //t.getLocalizedMessage(); toast
            }
        });
        return  data;
    }

    //add to user pref model
    public LiveData<AddToUserPrefResponseModel> fetchAddUserPref(String token, int course_id, int type){
        final  MutableLiveData<AddToUserPrefResponseModel> data=new MutableLiveData<>();
        apiInterface.addToUserPref(token,course_id,type).enqueue(new Callback<AddToUserPrefResponseModel>() {
            @Override
            public void onResponse(Call<AddToUserPrefResponseModel> call, Response<AddToUserPrefResponseModel> response) {
                AddToUserPrefResponseModel res=response.body();
                if (res!=null && res.getError_code() == 0){
                    data.setValue(res);
                    Log.d("TAGSETPREF","SHOW : "+res.getError_code());
                }else{
                    data.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<AddToUserPrefResponseModel> call, Throwable t) {
                data.setValue(null);
                //t.getLocalizedMessage(); toast
            }
        });
        return  data;
    }



}
