package com.prepare.prepareurself.favourites.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.favourites.data.db.dao.LikedProjectsDao;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class LikedProjectRepository {

    private LikedProjectsDao projectsRoomDao;
    private LiveData<List<LikedProjectsModel>> listLiveData;

    public LikedProjectRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        projectsRoomDao = appDatabase.likedProjectsDao();
    }

    public LiveData<List<LikedProjectsModel>> getAllProjects(){
        listLiveData = projectsRoomDao.getAllProjects();
        return listLiveData;
    }

    public LiveData<List<LikedProjectsModel>> getProjectsByCourseId(int courseId){
        return projectsRoomDao.getProjectsByCourseId(courseId);
    }

    public LiveData<List<LikedProjectsModel>> getProjectsByLevel(String level){
        listLiveData = projectsRoomDao.getProjectsByLevel(level);
        return listLiveData;
    }

    public LiveData<LikedProjectsModel> getProjectsById(int id){
        return projectsRoomDao.getProjectById(id);
    }

    public void insertProject(LikedProjectsModel projectsModel){
        new insertAsyncTask(projectsRoomDao).execute(projectsModel);
    }

    public void deleteAllProjects(){
        new deleteAllAsyncTask(projectsRoomDao).execute();
    }

    public LiveData<List<LikedProjectsModel>> getFiveProjectsByCourseId(int courseId) {
        return projectsRoomDao.getFiveProjectsByCourseId(courseId);
    }

    public void deleteLikedProject(LikedProjectsModel likedProjectsModel) {
        new deleteProjectAsyncTask(projectsRoomDao).execute(likedProjectsModel);
    }

    private static class insertAsyncTask extends AsyncTask<LikedProjectsModel,Void, Void> {

        private LikedProjectsDao dao;

        insertAsyncTask(LikedProjectsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(LikedProjectsModel... projectsModels) {
            dao.insertProject(projectsModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<LikedProjectsModel,Void, Void>{

        private LikedProjectsDao dao;

        deleteAllAsyncTask(LikedProjectsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(LikedProjectsModel... projectsModels) {
            dao.deleteAllProjects();

            return null;
        }
    }

    private static class deleteProjectAsyncTask extends AsyncTask<LikedProjectsModel,Void, Void>{

        private LikedProjectsDao dao;

        deleteProjectAsyncTask(LikedProjectsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(LikedProjectsModel... projectsModels) {
            dao.deleteProject(projectsModels[0].getId());

            return null;
        }
    }

}
