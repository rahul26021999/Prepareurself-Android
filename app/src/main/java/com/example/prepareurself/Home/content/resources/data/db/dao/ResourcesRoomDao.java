package com.example.prepareurself.Home.content.resources.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prepareurself.Home.content.resources.data.model.ResourceModel;

import java.util.List;

@Dao
public interface ResourcesRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertResource(ResourceModel resourceModel);

    @Query("SELECT * FROM resources WHERE course_topic_id=:course_topic_id AND type =:type")
    LiveData<List<ResourceModel>> getResourcesByID(int course_topic_id, String type);

    @Query("DELETE FROM resources")
    void deleteAllResources();

}
