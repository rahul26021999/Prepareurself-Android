package com.prepare.prepareurself.dashboard.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.dashboard.data.db.repository.CourseDbRepository;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.GetCourseResponseModel;
import com.prepare.prepareurself.utils.Utility;

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

                Log.d("course_api_debug",responseModel+" ,kj");

                if (responseModel!=null){
                    if (responseModel.getError_code()==0){
                        courseDbRepository.deleteAllCourses();
                        for (CourseModel course: responseModel.getCourses()) {
                            courseDbRepository.insertCourse(course);
                        }
                        data.setValue(responseModel.getCourses());
                    }else if (responseModel.getError_code()==1){
                        data.setValue(null);
                    }
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetCourseResponseModel> call, Throwable t) {
                data.setValue(null);
                Log.d("course_api_debug",t.getLocalizedMessage());

            }
        });

        return data;
    }

}
