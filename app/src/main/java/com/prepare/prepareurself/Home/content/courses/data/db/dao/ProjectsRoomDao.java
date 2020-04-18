package com.prepare.prepareurself.Home.content.courses.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;

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

    @Query("DELETE FROM projects")
    public void deleteAllProjects();

}
