package com.prepare.prepareurself.courses.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.courses.data.db.dao.TopicsRoomDao;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class TopicsDbRepository {

    private TopicsRoomDao topicsRoomDao;
    private LiveData<List<TopicsModel>> liveData;

    public TopicsDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        topicsRoomDao = db.topicsRoomDao();
    }

    public LiveData<List<TopicsModel>> getTopicsById(int courseId){
        liveData = topicsRoomDao.getTopics(courseId);
        return liveData;
    }

    public void insertTopic(TopicsModel topicsModel){
        new insertAsyncTask(topicsRoomDao).execute(topicsModel);
    }

    public void deleteAllTopics(){
        new deleteAllAsyncTask(topicsRoomDao).execute();
    }

    public LiveData<List<TopicsModel>> getFiveResourcesByID(int courseId) {
        return topicsRoomDao.getFiveTopicsByCourseId(courseId);
    }

    private static class insertAsyncTask extends AsyncTask<TopicsModel,Void, Void> {

        private TopicsRoomDao asynTopicsDao;

        insertAsyncTask(TopicsRoomDao dao){
            asynTopicsDao = dao;
        }

        @Override
        protected Void doInBackground(TopicsModel... topicsModels) {
            asynTopicsDao.insertTopic(topicsModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<TopicsModel,Void, Void>{

        private TopicsRoomDao asyncTopicsDao;

        deleteAllAsyncTask(TopicsRoomDao topicsRoomDao){
            asyncTopicsDao = topicsRoomDao;
        }

        @Override
        protected Void doInBackground(TopicsModel... topicsModels) {
            asyncTopicsDao.deleteAllTopics();

            return null;
        }
    }

}
