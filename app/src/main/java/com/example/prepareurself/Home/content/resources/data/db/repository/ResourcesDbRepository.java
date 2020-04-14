package com.example.prepareurself.Home.content.resources.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.prepareurself.Home.content.resources.data.db.dao.ResourcesRoomDao;
import com.example.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.example.prepareurself.persistence.AppDatabase;

import java.util.List;

public class ResourcesDbRepository {

    private ResourcesRoomDao resourcesRoomDao;
    private LiveData<List<ResourceModel>> listLiveData;

    public ResourcesDbRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        resourcesRoomDao = db.resourcesRoomDao();
    }

    public LiveData<List<ResourceModel>> getResourcesByID(int course_topic_id, String type){
        listLiveData = resourcesRoomDao.getResourcesByID(course_topic_id,type);
        return listLiveData;
    }

    public void insertResource(ResourceModel resourceModel){
        new insertAsyncResource(resourcesRoomDao).execute(resourceModel);
    }

    public void deleteAllResources(){
        new deleteAsyncResources(resourcesRoomDao).execute();
    }

    private class insertAsyncResource extends AsyncTask<ResourceModel, Void, Void> {
        private ResourcesRoomDao asyncResourcesRoomDao;

        public insertAsyncResource(ResourcesRoomDao asyncResourcesRoomDao) {
            this.asyncResourcesRoomDao = asyncResourcesRoomDao;
        }

        @Override
        protected Void doInBackground(ResourceModel... resourceModels) {
            asyncResourcesRoomDao.insertResource(resourceModels[0]);
            return null;
        }

    }

    private class deleteAsyncResources extends AsyncTask<ResourceModel, Void, Void>{
        private ResourcesRoomDao asyncResourceRoomDao;

        public deleteAsyncResources(ResourcesRoomDao resourcesRoomDao){
            this.asyncResourceRoomDao = resourcesRoomDao;
        }

        @Override
        protected Void doInBackground(ResourceModel... resourceModels) {

            asyncResourceRoomDao.deleteAllResources();

            return null;
        }
    }
}
