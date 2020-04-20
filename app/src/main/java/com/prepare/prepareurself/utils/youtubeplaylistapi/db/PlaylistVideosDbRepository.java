package com.prepare.prepareurself.utils.youtubeplaylistapi.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.persistence.AppDatabase;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

public class PlaylistVideosDbRepository {

    private PlaylistContentDeatilsDao playlistContentDeatilsDao;

    public PlaylistVideosDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        playlistContentDeatilsDao = db.playlistVideosDao();
    }

    public LiveData<List<VideoItemWrapper>> getVideoContentDetails(){
        return playlistContentDeatilsDao.getVideoItemWrappers();
    }

    public LiveData<List<VideoItemWrapper>> getVideoItemWrapperByPlaylistId(String playlistId){
        return playlistContentDeatilsDao.getVideoItemWrapperByPlaylistId(playlistId);
    }

    public void insertVideoContentDetail(VideoItemWrapper videoContentDetails){
        new insertAsyncTask(playlistContentDeatilsDao).execute(videoContentDetails);
    }

    public void deleteVideoContentDetails(){
        new deleteAsyncTask(playlistContentDeatilsDao).execute();
    }


    private static class insertAsyncTask extends AsyncTask<VideoItemWrapper,Void, Void> {

        private PlaylistContentDeatilsDao videosDao;

        insertAsyncTask(PlaylistContentDeatilsDao dao){
            videosDao = dao;
        }

        @Override
        protected Void doInBackground(VideoItemWrapper... videoContentDetails) {
            videosDao.insertVideoItemWrapper(videoContentDetails[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<VideoItemWrapper,Void, Void>{

        private PlaylistContentDeatilsDao videosDao;

        deleteAsyncTask(PlaylistContentDeatilsDao dao){
            videosDao = dao;
        }

        @Override
        protected Void doInBackground(VideoItemWrapper... videoContentDetails) {
            videosDao.deleteVideoItemWrappers();

            return null;
        }
    }

}
