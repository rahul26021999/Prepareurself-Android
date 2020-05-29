package com.prepare.prepareurself.dashboard.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.dashboard.data.db.dao.HomePageDao;
import com.prepare.prepareurself.dashboard.data.model.HomepageData;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class HomePageDbRepository {

    private HomePageDao homePageDao;
    private LiveData<List<HomepageData>> data;

    public HomePageDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        homePageDao = db.homePageDao();

    }

    public LiveData<List<HomepageData>> getHomePageData(){
        return homePageDao.getHomePageData();
    }

    public void insertHomePageData(HomepageData homepageData){
        Log.d("home_page_debug",homepageData.getType()+" abc");
        new insertAsyncTask(homePageDao).execute(homepageData);
    }

    public void deleteHomePageData(){
        new deleteAllAsyncTask(homePageDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<HomepageData,Void, Void> {

        private HomePageDao asynCourseDao;

        insertAsyncTask(HomePageDao dao){
            asynCourseDao = dao;
        }

        @Override
        protected Void doInBackground(HomepageData... courseModels) {
            asynCourseDao.insertHomePageData(courseModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<HomepageData,Void, Void>{

        private HomePageDao asyncCourseDao;

        deleteAllAsyncTask(HomePageDao courseRoomDao){
            asyncCourseDao = courseRoomDao;
        }

        @Override
        protected Void doInBackground(HomepageData... userModels) {
            asyncCourseDao.deleteHomePageData();

            return null;
        }
    }


}
