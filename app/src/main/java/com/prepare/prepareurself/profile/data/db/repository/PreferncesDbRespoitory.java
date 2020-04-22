package com.prepare.prepareurself.profile.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.courses.data.db.dao.ProjectsRoomDao;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.persistence.AppDatabase;
import com.prepare.prepareurself.profile.data.db.dao.UserPrefernceDao;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;

import java.util.List;

public class PreferncesDbRespoitory {

    private UserPrefernceDao userPrefernceDao;

    public PreferncesDbRespoitory(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        userPrefernceDao = db.userPrefernceDao();
    }

    public LiveData<List<PreferredTechStack>> getAllPreferences(){
        return userPrefernceDao.getPreferences();
    }

    public void insertPreference(PreferredTechStack preferredTechStack){
        new insertAsyncTask(userPrefernceDao).execute(preferredTechStack);
    }

    public void deletePreferences(){
        new deleteAllAsyncTask(userPrefernceDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<PreferredTechStack,Void, Void> {

        private UserPrefernceDao userPrefernceDao;

        insertAsyncTask(UserPrefernceDao dao){
            userPrefernceDao = dao;
        }

        @Override
        protected Void doInBackground(PreferredTechStack... pref) {
            userPrefernceDao.insertPreference(pref[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<PreferredTechStack,Void, Void>{

        private UserPrefernceDao prefernceDao;

        deleteAllAsyncTask(UserPrefernceDao dao){
            prefernceDao = dao;
        }

        @Override
        protected Void doInBackground(PreferredTechStack... preferredTechStacks) {
            prefernceDao.deleteAllPreferences();

            return null;
        }
    }

}
