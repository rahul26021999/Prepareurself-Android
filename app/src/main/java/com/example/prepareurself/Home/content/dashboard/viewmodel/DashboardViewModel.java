package com.example.prepareurself.Home.content.dashboard.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prepareurself.Home.content.dashboard.data.db.repository.CourseDbRepository;
import com.example.prepareurself.Home.content.dashboard.data.model.CourseModel;
import com.example.prepareurself.Home.content.dashboard.data.model.DashboardRecyclerviewModel;
import com.example.prepareurself.Home.content.dashboard.data.repository.CourseRepository;
import com.example.prepareurself.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.prepareurself.utils.Constants.COURSEVIEWTYPE;

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
