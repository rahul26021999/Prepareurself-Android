package com.prepare.prepareurself.dashboard.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;

import java.util.List;

@Dao
public interface SuggestedTopicsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopic(SuggestedTopicsModel topicsModel);

    @Query("SELECT * FROM suggested_topics WHERE course_id=:courseId")
    LiveData<List<SuggestedTopicsModel>> getTopics(int courseId);

    @Query("DELETE FROM suggested_topics")
    void deleteAllTopics();

    @Query("SELECT * FROM suggested_topics WHERE course_id=:courseId LIMIT 5")
    LiveData<List<SuggestedTopicsModel>> getFiveTopicsByCourseId(int courseId);

    @Query("SELECT * FROM suggested_projects")
    LiveData<List<SuggestedTopicsModel>> getAllTopics();
}
