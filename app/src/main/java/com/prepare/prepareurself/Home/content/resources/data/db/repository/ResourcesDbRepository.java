package com.prepare.prepareurself.Home.content.resources.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.Home.content.resources.data.db.dao.ResourcesRoomDao;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.persistence.AppDatabase;

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

    public LiveData<List<ResourceModel>> getResourceResourcesExcept(int topic_id, String type, int resource_id){
        listLiveData = resourcesRoomDao.getResourceByIdExcept(topic_id,type,resource_id);
        return listLiveData;
    }

    public void insertResource(ResourceModel resourceModel){
        new insertAsyncResource(resourcesRoomDao).execute(resourceModel);
    }

    public void deleteAllResources(){
        new deleteAsyncResources(resourcesRoomDao).execute();
    }

    public LiveData<ResourceModel> getResourceByResourceId(int resourceId, String type) {
        return resourcesRoomDao.getResourceByResourceId(resourceId,type);
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
