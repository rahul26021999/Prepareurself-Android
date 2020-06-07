package com.prepare.prepareurself.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;
import com.prepare.prepareurself.courses.data.repository.CourseDetailRepository;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailViewModel extends AndroidViewModel {

    private CourseDetailRepository courseDetailRepository;
    public LiveData<CourseDetailReponseModel> courseDetailReponseModelLiveData = new MutableLiveData<>();
    public LiveData<RateCourseResponseModel> rateCourseResponseModelLiveData=new MutableLiveData<>();

    public CourseDetailViewModel(@NonNull Application application) {
        super(application);
        courseDetailRepository = new CourseDetailRepository();
    }

    public void fetchCourseDetails(String token, int courseId){
        courseDetailReponseModelLiveData = courseDetailRepository.fetchCourseDetails(token, courseId);
    }
    public void fetchRateCourse(String token, int courseId, int rating){
        rateCourseResponseModelLiveData=courseDetailRepository.fetchRateCourse(token,courseId,rating);
    }

}
