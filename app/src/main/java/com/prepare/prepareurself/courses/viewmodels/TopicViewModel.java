package com.prepare.prepareurself.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.courses.data.db.repository.TopicsDbRepository;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.courses.data.repository.TopicsRepository;
import com.prepare.prepareurself.dashboard.data.db.repository.CourseDbRepository;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {

    private LiveData<List<TopicsModel>> liveData = new MutableLiveData<>();
    private LiveData<TopicsResponseModel> topicsResponseModelLiveData = new MutableLiveData<>();
    TopicsDbRepository topicsDbRepository;
    TopicsRepository topicsRepository;
    CourseDbRepository courseDbRepository;

    public TopicViewModel(@NonNull Application application) {
        super(application);
        topicsRepository = new TopicsRepository(application);
        topicsDbRepository = new TopicsDbRepository(application);
        courseDbRepository = new CourseDbRepository(application);
    }

    public LiveData<TopicsResponseModel> getTopicsResponseModelLiveData() {
        return topicsResponseModelLiveData;
    }

    public void getCourseById(String token, int courseId, int count, int pageNumber){
        topicsResponseModelLiveData = topicsRepository.getTopicsByIId(token,courseId, count, pageNumber);
    }

    public LiveData<List<TopicsModel>> getLiveData(int courseId) {
        if (liveData.getValue()==null){
            liveData = topicsDbRepository.getTopicsById(courseId);
        }
        return liveData;
    }

    public LiveData<CourseModel> getCourseModelById(int courseId){
        return courseDbRepository.getCourseById(courseId);
    }
}
