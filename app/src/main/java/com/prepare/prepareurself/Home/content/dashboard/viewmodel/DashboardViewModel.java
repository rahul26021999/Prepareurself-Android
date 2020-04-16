package com.prepare.prepareurself.Home.content.dashboard.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.dashboard.data.db.repository.CourseDbRepository;
import com.prepare.prepareurself.Home.content.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.Home.content.dashboard.data.repository.CourseRepository;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private LiveData<List<CourseModel>> liveCourses = new MutableLiveData<>();
    CourseRepository courseRepository;
    CourseDbRepository courseDbRepository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        courseDbRepository = new CourseDbRepository(application);
    }

    public void getCourses(String token){
        liveCourses = courseRepository.getCourses(token);
    }

    public LiveData<List<CourseModel>> getLiveCourses(){
        if (liveCourses.getValue()==null){
            liveCourses = courseDbRepository.getAllCourses();
        }
        return liveCourses;
    }

}