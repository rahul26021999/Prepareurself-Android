package com.prepare.prepareurself.dashboard.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;

import java.util.List;

@Dao
public interface SuggestedProjectsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertProject(SuggestedProjectModel projectsModel);

    @Query("SELECT * FROM suggested_projects")
    public LiveData<List<SuggestedProjectModel>> getAllProjects();

    @Query("SELECT * FROM suggested_projects WHERE level=:level")
    public LiveData<List<SuggestedProjectModel>> getProjectsByLevel(String level);

    @Query("SELECT * FROM suggested_projects WHERE id=:id")
    public LiveData<SuggestedProjectModel> getProjectById(int id);

    @Query("SELECT * FROM suggested_projects WHERE course_id=:courseId")
    LiveData<List<SuggestedProjectModel>> getProjectsByCourseId(int courseId);

    @Query("DELETE FROM suggested_projects")
    public void deleteAllProjects();

    @Query("SELECT * FROM suggested_projects WHERE course_id=:courseId LIMIT 5")
    LiveData<List<SuggestedProjectModel>> getFiveProjectsByCourseId(int courseId);

}
