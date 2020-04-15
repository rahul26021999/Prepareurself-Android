package com.prepare.prepareurself.Home.content.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.courses.data.db.repository.TopicsDbRepository;
import com.prepare.prepareurself.Home.content.courses.data.model.TopicsModel;
import com.prepare.prepareurself.Home.content.courses.data.repository.TopicsRepository;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {

    private LiveData<List<TopicsModel>> liveData = new MutableLiveData<>();
    TopicsDbRepository topicsDbRepository;
    TopicsRepository topicsRepository;

    public TopicViewModel(@NonNull Application application) {
        super(application);
        topicsRepository = new TopicsRepository(application);
        topicsDbRepository = new TopicsDbRepository(application);
    }

    public void getCourseById(String token, int courseId){
        liveData = topicsRepository.getTopicsByIId(token,courseId);
    }

    public LiveData<List<TopicsModel>> getLiveData(int courseId) {
        if (liveData.getValue()==null){
            liveData = topicsDbRepository.getTopicsById(courseId);
        }
        return liveData;
    }
}
