package com.prepare.prepareurself.dashboard.data.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.dashboard.data.model.CourseModel;

import java.util.List;

@Dao
public interface CourseRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CourseModel courseModel);

    @Query("SELECT * FROM course")
    LiveData<List<CourseModel>> getAllCourses();

    @Query("DELETE FROM course")
    void deleteAllCourses();

    @Query("SELECT * FROM course LIMIT 5")
    LiveData<List<CourseModel>> getFiveCourses();
}
