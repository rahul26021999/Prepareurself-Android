package com.prepare.prepareurself.dashboard.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.dashboard.data.db.dao.BannerImageRoomDao;
import com.prepare.prepareurself.dashboard.data.model.BannerModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class BannerDbRepository {

    BannerImageRoomDao bannerImageRoomDao;

    public BannerDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        this.bannerImageRoomDao = db.bannerImageRoomDao();
    }

    public LiveData<List<BannerModel>> getAllBanners(){
        return bannerImageRoomDao.getBanners();
    }

    public void insertBanner(BannerModel bannerModel){
        new insertAsyncTask(bannerImageRoomDao).execute(bannerModel);
    }

    public void deleteAllBanners(){
        new deleteAllAsyncTask(bannerImageRoomDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<BannerModel,Void, Void> {

        private BannerImageRoomDao bannerDao;

        insertAsyncTask(BannerImageRoomDao dao){
            bannerDao = dao;
        }

        @Override
        protected Void doInBackground(BannerModel... courseModels) {
            bannerDao.insertBanner(courseModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<BannerModel,Void, Void>{

        private BannerImageRoomDao bannerImageRoomDao;

        deleteAllAsyncTask(BannerImageRoomDao dao){
            bannerImageRoomDao = dao;
        }

        @Override
        protected Void doInBackground(BannerModel... userModels) {
            bannerImageRoomDao.deleteBanners();

            return null;
        }
    }

}
