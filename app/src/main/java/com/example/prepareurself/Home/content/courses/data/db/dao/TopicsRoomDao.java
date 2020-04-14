package com.example.prepareurself.Home.content.courses.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prepareurself.Home.content.courses.data.model.TopicsModel;

import java.util.List;

@Dao
public interface TopicsRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopic(TopicsModel topicsModel);

    @Query("SELECT * FROM topics WHERE course_id=:courseId")
    LiveData<List<TopicsModel>> getTopics(int courseId);

    @Query("DELETE FROM topics")
    void deleteAllTopics();

}
