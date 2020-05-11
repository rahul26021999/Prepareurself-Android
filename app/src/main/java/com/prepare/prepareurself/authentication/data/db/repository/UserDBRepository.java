package com.prepare.prepareurself.authentication.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.persistence.AppDatabase;

public class UserDBRepository {

    private UserRoomDao userRoomDao;
    LiveData<UserModel> userRoomModelLiveData;

    public UserDBRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        userRoomDao = db.userRoomDao();
        userRoomModelLiveData = userRoomDao.getUserInfo();
    }

    public LiveData<UserModel> getUserInfo(){
        return userRoomModelLiveData;
    }

    public void insertUser(UserModel userModel){
        new clearDataAsyncTask(userRoomDao).execute(userModel);
    }

    public void clearUser(){
        new deleteAsyncTask(userRoomDao);
    }

    private static class insertAsyncTask extends AsyncTask<UserModel,Void, Void>{

        private UserRoomDao asyncUserDao;

        insertAsyncTask(UserRoomDao userRoomDao){
            asyncUserDao = userRoomDao;
        }

        @Override
        protected Void doInBackground(UserModel... userModels) {
            asyncUserDao.insert(userModels[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<UserModel,Void, Void>{

        private UserRoomDao asyncUserDao;

        deleteAsyncTask(UserRoomDao userRoomDao){
            asyncUserDao = userRoomDao;
        }

        @Override
        protected Void doInBackground(UserModel... userModels) {
            asyncUserDao.deleteAll();

            return null;
        }
    }

    private static class clearDataAsyncTask extends AsyncTask<UserModel,Void, Void>{

        private UserRoomDao asyncUserDao;

        clearDataAsyncTask(UserRoomDao userRoomDao){
            asyncUserDao = userRoomDao;
        }

        @Override
        protected Void doInBackground(UserModel... userModels) {
            asyncUserDao.deleteAll();

            new insertAsyncTask(asyncUserDao).execute(userModels[0]);

            return null;
        }
    }

}
