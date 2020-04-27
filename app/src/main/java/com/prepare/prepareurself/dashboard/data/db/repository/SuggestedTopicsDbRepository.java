package com.prepare.prepareurself.dashboard.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.dashboard.data.db.dao.SuggestedTopicsDao;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class SuggestedTopicsDbRepository {

    private SuggestedTopicsDao suggestedTopicsDao;
    private LiveData<List<SuggestedTopicsModel>> liveData;

    public SuggestedTopicsDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        suggestedTopicsDao = db.suggestedTopicsDao();
    }

    public LiveData<List<SuggestedTopicsModel>> getTopicsById(int courseId){
        liveData = suggestedTopicsDao.getTopics(courseId);
        return liveData;
    }

    public void insertTopic(SuggestedTopicsModel topicsModel){
        new insertAsyncTask(suggestedTopicsDao).execute(topicsModel);
    }

    public void deleteAllTopics(){
        new deleteAllAsyncTask(suggestedTopicsDao).execute();
    }

    public LiveData<List<SuggestedTopicsModel>> getFiveResourcesByID(int courseId) {
        return suggestedTopicsDao.getFiveTopicsByCourseId(courseId);
    }

    public LiveData<List<SuggestedTopicsModel>> getAllTopics() {
        return suggestedTopicsDao.getAllTopics();
    }

    private static class insertAsyncTask extends AsyncTask<SuggestedTopicsModel,Void, Void> {

        private SuggestedTopicsDao suggestedTopicsDao1;

        insertAsyncTask(SuggestedTopicsDao dao){
            suggestedTopicsDao1 = dao;
        }

        @Override
        protected Void doInBackground(SuggestedTopicsModel... topicsModels) {
            suggestedTopicsDao1.insertTopic(topicsModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<SuggestedTopicsModel,Void, Void>{

        private SuggestedTopicsDao suggestedTopicsDao1;

        deleteAllAsyncTask(SuggestedTopicsDao topicsRoomDao){
            suggestedTopicsDao1 = topicsRoomDao;
        }

        @Override
        protected Void doInBackground(SuggestedTopicsModel... topicsModels) {
            suggestedTopicsDao1.deleteAllTopics();

            return null;
        }
    }

}
