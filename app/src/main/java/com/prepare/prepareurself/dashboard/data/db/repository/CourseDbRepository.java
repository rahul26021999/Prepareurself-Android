package com.prepare.prepareurself.dashboard.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.dashboard.data.db.dao.CourseRoomDao;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.persistence.AppDatabase;

import java.util.List;

public class CourseDbRepository {
    private CourseRoomDao courseRoomDao;
    private LiveData<List<CourseModel>> listLiveData;

    public CourseDbRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        courseRoomDao = db.courseRoomDao();
        listLiveData = courseRoomDao.getAllCourses();
    }

    public LiveData<List<CourseModel>> getAllCourses(){
        return listLiveData;
    }

    public void insertCourse(CourseModel courseModel){
        new insertAsyncTask(courseRoomDao).execute(courseModel);
    }

    public void deleteAllCourses(){
        new deleteAllAsyncTask(courseRoomDao).execute();
    }

    public LiveData<List<CourseModel>> getFiveCourses() {
        return courseRoomDao.getFiveCourses();
    }

    private static class insertAsyncTask extends AsyncTask<CourseModel,Void, Void> {

        private CourseRoomDao asynCourseDao;

        insertAsyncTask(CourseRoomDao dao){
            asynCourseDao = dao;
        }

        @Override
        protected Void doInBackground(CourseModel... courseModels) {
            asynCourseDao.insert(courseModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<CourseModel,Void, Void>{

        private CourseRoomDao asyncCourseDao;

        deleteAllAsyncTask(CourseRoomDao courseRoomDao){
            asyncCourseDao = courseRoomDao;
        }

        @Override
        protected Void doInBackground(CourseModel... userModels) {
            asyncCourseDao.deleteAllCourses();

            return null;
        }
    }

}
