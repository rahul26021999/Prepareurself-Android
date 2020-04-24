package com.prepare.prepareurself.profile.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.persistence.AppDatabase;
import com.prepare.prepareurself.profile.data.db.dao.MyPreferenceDao;
import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;

import java.util.List;

public class MyPreferenceDbRepository {

    private MyPreferenceDao userPrefernceDao;

    public MyPreferenceDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        userPrefernceDao = db.myPreferenceDao();
    }

    public LiveData<List<MyPreferenceTechStack>> getAllPreferences(){
        return userPrefernceDao.getPreferences();
    }

    public void insertPreference(MyPreferenceTechStack preferredTechStack){
        new insertAsyncTask(userPrefernceDao).execute(preferredTechStack);
    }

    public void deletePreferences(){
        new deleteAllAsyncTask(userPrefernceDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<MyPreferenceTechStack,Void, Void> {

        private MyPreferenceDao userPrefernceDao;

        insertAsyncTask(MyPreferenceDao dao){
            userPrefernceDao = dao;
        }

        @Override
        protected Void doInBackground(MyPreferenceTechStack... pref) {
            userPrefernceDao.insertPreference(pref[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<MyPreferenceTechStack,Void, Void>{

        private MyPreferenceDao prefernceDao;

        deleteAllAsyncTask(MyPreferenceDao dao){
            prefernceDao = dao;
        }

        @Override
        protected Void doInBackground(MyPreferenceTechStack... preferredTechStacks) {
            prefernceDao.deleteAllPreferences();

            return null;
        }
    }

}
