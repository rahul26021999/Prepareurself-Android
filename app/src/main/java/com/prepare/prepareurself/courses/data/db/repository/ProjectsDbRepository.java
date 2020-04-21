package com.prepare.prepareurself.courses.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.courses.data.db.dao.ProjectsRoomDao;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class ProjectsDbRepository {

    private ProjectsRoomDao projectsRoomDao;
    private LiveData<List<ProjectsModel>> listLiveData;

    public ProjectsDbRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        projectsRoomDao = appDatabase.projectsRoomDao();
    }

    public LiveData<List<ProjectsModel>> getAllProjects(){
        listLiveData = projectsRoomDao.getAllProjects();
        return listLiveData;
    }

    public LiveData<List<ProjectsModel>> getProjectsByLevel(String level){
        listLiveData = projectsRoomDao.getProjectsByLevel(level);
        return listLiveData;
    }

    public LiveData<ProjectsModel> getProjectsById(int id){
        return projectsRoomDao.getProjectById(id);
    }

    public void insertProject(ProjectsModel projectsModel){
        new insertAsyncTask(projectsRoomDao).execute(projectsModel);
    }

    public void deleteAllProjects(){
        new deleteAllAsyncTask(projectsRoomDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<ProjectsModel,Void, Void> {

        private ProjectsRoomDao projectsRoomDao;

        insertAsyncTask(ProjectsRoomDao dao){
            projectsRoomDao = dao;
        }

        @Override
        protected Void doInBackground(ProjectsModel... projectsModels) {
            projectsRoomDao.insertProject(projectsModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<ProjectsModel,Void, Void>{

        private ProjectsRoomDao projectsRoomDao;

        deleteAllAsyncTask(ProjectsRoomDao dao){
            projectsRoomDao = dao;
        }

        @Override
        protected Void doInBackground(ProjectsModel... projectsModels) {
            projectsRoomDao.deleteAllProjects();

            return null;
        }
    }

}
