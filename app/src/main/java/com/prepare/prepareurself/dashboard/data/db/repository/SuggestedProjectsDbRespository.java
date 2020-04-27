package com.prepare.prepareurself.dashboard.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.dashboard.data.db.dao.SuggestedProjectsDao;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class SuggestedProjectsDbRespository {

    private SuggestedProjectsDao suggestedProjectsDao;
    private LiveData<List<SuggestedProjectModel>> listLiveData;

    public SuggestedProjectsDbRespository(Application application){
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        suggestedProjectsDao = appDatabase.suggestedProjectsDao();
    }

    public LiveData<List<SuggestedProjectModel>> getAllProjects(){
        listLiveData = suggestedProjectsDao.getAllProjects();
        return listLiveData;
    }

    public LiveData<List<SuggestedProjectModel>> getProjectsByCourseId(int courseId){
        return suggestedProjectsDao.getProjectsByCourseId(courseId);
    }

    public LiveData<List<SuggestedProjectModel>> getProjectsByLevel(String level){
        listLiveData = suggestedProjectsDao.getProjectsByLevel(level);
        return listLiveData;
    }

    public LiveData<SuggestedProjectModel> getProjectsById(int id){
        return suggestedProjectsDao.getProjectById(id);
    }

    public void insertProject(SuggestedProjectModel projectsModel){
        new insertAsyncTask(suggestedProjectsDao).execute(projectsModel);
    }

    public void deleteAllProjects(){
        new deleteAllAsyncTask(suggestedProjectsDao).execute();
    }

    public LiveData<List<SuggestedProjectModel>> getFiveProjectsByCourseId(int courseId) {
        return suggestedProjectsDao.getFiveProjectsByCourseId(courseId);
    }

    private static class insertAsyncTask extends AsyncTask<SuggestedProjectModel,Void, Void> {

        private SuggestedProjectsDao projectsRoomDao;

        insertAsyncTask(SuggestedProjectsDao dao){
            projectsRoomDao = dao;
        }

        @Override
        protected Void doInBackground(SuggestedProjectModel... projectsModels) {
            projectsRoomDao.insertProject(projectsModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<SuggestedProjectModel,Void, Void>{

        private SuggestedProjectsDao suggestedProjectsDao1;

        deleteAllAsyncTask(SuggestedProjectsDao dao){
            suggestedProjectsDao1 = dao;
        }

        @Override
        protected Void doInBackground(SuggestedProjectModel... projectsModels) {
            suggestedProjectsDao1.deleteAllProjects();

            return null;
        }
    }

}
