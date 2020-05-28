package com.prepare.prepareurself.favourites.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.favourites.data.db.dao.LikedProjectsDao;
import com.prepare.prepareurself.favourites.data.db.dao.LikedResourcesDao;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class LikedResourceRespository {

    private LikedResourcesDao resourcesDao;
    private LiveData<List<LikedResourcesModel>> listLiveData;

    public LikedResourceRespository(Application application){
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        resourcesDao = appDatabase.likedResourcesDao();
    }

    public LiveData<List<LikedResourcesModel>> getAllResources(){
        listLiveData = resourcesDao.getAllResources();
        return listLiveData;
    }

    public void insertResource(LikedResourcesModel projectsModel){
        new insertAsyncTask(resourcesDao).execute(projectsModel);
    }

    public LiveData<List<LikedResourcesModel>> getLikedResourcesExceptId(int resourceId, String type){
        return resourcesDao.getResourceByIdExcept(type, resourceId);
    }

    public void deleteResource(LikedResourcesModel likedResourcesModel){
        new deleteResourceAsyncTask(resourcesDao).execute(likedResourcesModel);
    }

    public void deleteAllResources(){
        new deleteAllAsyncTask(resourcesDao).execute();
    }


    private static class insertAsyncTask extends AsyncTask<LikedResourcesModel,Void, Void> {

        private LikedResourcesDao dao;

        insertAsyncTask(LikedResourcesDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(LikedResourcesModel... projectsModels) {
            dao.insertResource(projectsModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<LikedResourcesModel,Void, Void>{

        private LikedResourcesDao dao;

        deleteAllAsyncTask(LikedResourcesDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(LikedResourcesModel... projectsModels) {
            dao.deleteAllResources();

            return null;
        }
    }

    private static class deleteResourceAsyncTask extends AsyncTask<LikedResourcesModel,Void, Void>{

        private LikedResourcesDao dao;

        deleteResourceAsyncTask(LikedResourcesDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(LikedResourcesModel... projectsModels) {
            dao.deleteResource(projectsModels[0].getId());

            return null;
        }
    }

}
