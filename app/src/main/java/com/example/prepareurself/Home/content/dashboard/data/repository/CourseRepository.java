package com.example.prepareurself.Home.content.dashboard.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prepareurself.Apiservice.ApiClient;
import com.example.prepareurself.Apiservice.ApiInterface;
import com.example.prepareurself.Home.content.dashboard.data.db.repository.CourseDbRepository;
import com.example.prepareurself.Home.content.dashboard.data.model.CourseModel;
import com.example.prepareurself.Home.content.dashboard.data.model.GetCourseResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRepository {

    private ApiInterface apiInterface;
    private CourseDbRepository courseDbRepository;

    public CourseRepository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        courseDbRepository = new CourseDbRepository(application);
    }

    public LiveData<List<CourseModel>> getCourses(String token){
        final MutableLiveData<List<CourseModel>> data = new MutableLiveData<>();

        apiInterface.getCourses(token).enqueue(new Callback<GetCourseResponseModel>() {
            @Override
            public void onResponse(Call<GetCourseResponseModel> call, Response<GetCourseResponseModel> response) {
                GetCourseResponseModel responseModel = response.body();
                if (responseModel!=null){
                    if (responseModel.getError_code()==0){
                        courseDbRepository.deleteAllCourses();
                        for (CourseModel course: responseModel.getCourses()) {
                            courseDbRepository.insertCourse(course);
                        }
                        data.setValue(courseDbRepository.getAllCourses().getValue());
                    }else{
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetCourseResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
