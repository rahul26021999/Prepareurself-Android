package com.example.prepareurself.persistence.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.prepareurself.persistence.model.UserRoomModel;

public class AppDbRepository {

    private UserRoomDao userRoomDao;
    LiveData<UserRoomModel> userRoomModelLiveData;

    public AppDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        userRoomDao = db.userRoomDao();
        userRoomModelLiveData = userRoomDao.getUserInfo();
    }

    public LiveData<UserRoomModel> getUserInfo(){
        return userRoomModelLiveData;
    }

    public void insertUser(UserRoomModel userRoomModel){
        new insertAsyncTask(userRoomDao).execute(userRoomModel);
    }

    private static class insertAsyncTask extends AsyncTask<UserRoomModel,Void, Void>{

        private UserRoomDao asyncUserDao;

        insertAsyncTask(UserRoomDao userRoomDao){
            asyncUserDao = userRoomDao;
        }

        @Override
        protected Void doInBackground(UserRoomModel... userRoomModels) {
            asyncUserDao.insert(userRoomModels[0]);

            return null;
        }
    }

}
