package com.prepare.prepareurself.courses.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.courses.data.model.ProjectsModel;

import java.util.List;

@Dao
public interface ProjectsRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertProject(ProjectsModel projectsModel);

    @Query("SELECT * FROM projects")
    public LiveData<List<ProjectsModel>> getAllProjects();

    @Query("SELECT * FROM projects WHERE level=:level")
    public LiveData<List<ProjectsModel>> getProjectsByLevel(String level);

    @Query("SELECT * FROM projects WHERE id=:id")
    public LiveData<ProjectsModel> getProjectById(int id);

    @Query("SELECT * FROM projects WHERE course_id=:courseId")
    LiveData<List<ProjectsModel>> getProjectsByCourseId(int courseId);

    @Query("DELETE FROM projects")
    public void deleteAllProjects();

    @Query("SELECT * FROM projects WHERE course_id=:courseId LIMIT 5")
    LiveData<List<ProjectsModel>> getFiveProjectsByCourseId(int courseId);
}
