package com.prepare.prepareurself.courses.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.courses.data.model.TopicsModel;

import java.util.List;

@Dao
public interface TopicsRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopic(TopicsModel topicsModel);

    @Query("SELECT * FROM topics WHERE course_id=:courseId ORDER BY sequence ASC")
    LiveData<List<TopicsModel>> getTopics(int courseId);

    @Query("DELETE FROM topics")
    void deleteAllTopics();

    @Query("SELECT * FROM topics WHERE course_id=:courseId LIMIT 5")
    LiveData<List<TopicsModel>> getFiveTopicsByCourseId(int courseId);
}
