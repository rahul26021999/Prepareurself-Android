package com.prepare.prepareurself.utils.youtubeplaylistapi.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.persistence.AppDatabase;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;

import java.util.List;

public class PlaylistVideosDbRepository {

    private PlaylistVideosDao playlistVideosDao;

    public PlaylistVideosDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        playlistVideosDao = db.playlistVideosDao();
    }

    public LiveData<List<VideoContentDetails>> getVideoContentDetails(){
        return playlistVideosDao.getVideoContentDetails();
    }

    public void insertVideoContentDetail(VideoContentDetails videoContentDetails){
        new insertAsyncTask(playlistVideosDao).execute(videoContentDetails);
    }

    public void deleteVideoContentDetails(){
        new deleteAsyncTask(playlistVideosDao).execute();
    }


    private static class insertAsyncTask extends AsyncTask<VideoContentDetails,Void, Void> {

        private PlaylistVideosDao videosDao;

        insertAsyncTask(PlaylistVideosDao dao){
            videosDao = dao;
        }

        @Override
        protected Void doInBackground(VideoContentDetails... videoContentDetails) {
            videosDao.insertVideoContent(videoContentDetails[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<VideoContentDetails,Void, Void>{

        private PlaylistVideosDao videosDao;

        deleteAsyncTask(PlaylistVideosDao dao){
            videosDao = dao;
        }

        @Override
        protected Void doInBackground(VideoContentDetails... videoContentDetails) {
            videosDao.deleteVideoContentDetails();

            return null;
        }
    }

}
