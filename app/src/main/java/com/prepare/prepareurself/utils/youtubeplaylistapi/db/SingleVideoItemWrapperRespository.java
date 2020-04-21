package com.prepare.prepareurself.utils.youtubeplaylistapi.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.persistence.AppDatabase;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.SingleVIdeoItemWrapper;

import java.util.List;

public class SingleVideoItemWrapperRespository {

    private SingleVideoItemDao singleVideoItemDao;

    public SingleVideoItemWrapperRespository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        singleVideoItemDao = db.singleVideoItemDao();
    }

    public LiveData<SingleVIdeoItemWrapper> getVideoContentDetails(String videoCode){
        return singleVideoItemDao.getVideoItemWrappers(videoCode);
    }

    public void insertVideoContentDetail(SingleVIdeoItemWrapper videoContentDetails){
        new insertAsyncTask(singleVideoItemDao).execute(videoContentDetails);
    }

    public void deleteVideoContentDetails(){
        new deleteAsyncTask(singleVideoItemDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<SingleVIdeoItemWrapper,Void, Void> {

        private SingleVideoItemDao videosDao;

        insertAsyncTask(SingleVideoItemDao dao){
            videosDao = dao;
        }

        @Override
        protected Void doInBackground(SingleVIdeoItemWrapper... videoContentDetails) {
            videosDao.insertVideoItemWrapper(videoContentDetails[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<SingleVIdeoItemWrapper,Void, Void>{

        private SingleVideoItemDao videosDao;

        deleteAsyncTask(SingleVideoItemDao dao){
            videosDao = dao;
        }

        @Override
        protected Void doInBackground(SingleVIdeoItemWrapper... videoContentDetails) {
            videosDao.deleteVideoItemWrappers();

            return null;
        }
    }

}
