package com.prepare.prepareurself.favourites.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;

import java.util.List;

@Dao
public interface LikedProjectsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertProject(LikedProjectsModel projectsModel);

    @Query("SELECT * FROM likedprojects ORDER BY sequence ASC")
    public LiveData<List<LikedProjectsModel>> getAllProjects();

    @Query("SELECT * FROM likedprojects WHERE level=:level ORDER by sequence ASC")
    public LiveData<List<LikedProjectsModel>> getProjectsByLevel(String level);

    @Query("SELECT * FROM likedprojects WHERE id=:id ORDER BY sequence ASC")
    public LiveData<LikedProjectsModel> getProjectById(int id);

    @Query("SELECT * FROM likedprojects WHERE course_id=:courseId ORDER BY sequence ASC")
    LiveData<List<LikedProjectsModel>> getProjectsByCourseId(int courseId);

    @Query("DELETE FROM likedprojects")
    public void deleteAllProjects();

    @Query("SELECT * FROM likedprojects WHERE course_id=:courseId ORDER BY sequence ASC LIMIT 5")
    LiveData<List<LikedProjectsModel>> getFiveProjectsByCourseId(int courseId);

    @Query("DELETE FROM likedprojects WHERE id=:projectId")
    void deleteProject(int projectId);
}
