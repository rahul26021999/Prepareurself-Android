package com.prepare.prepareurself.resources.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.util.List;

@Dao
public interface ResourcesRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertResource(ResourceModel resourceModel);

    @Query("SELECT * FROM resources WHERE course_topic_id=:course_topic_id AND type =:type")
    LiveData<List<ResourceModel>> getResourcesByID(int course_topic_id, String type);

    @Query("SELECT * FROM resources WHERE course_topic_id=:topic_id AND type =:type AND id != :resource_id")
    LiveData<List<ResourceModel>> getResourceByIdExcept(int topic_id, String type, int resource_id);

    @Query("SELECT * FROM resources WHERE id=:resourceId AND type=:type")
    LiveData<ResourceModel> getResourceByResourceId(int resourceId, String type);

    @Query("DELETE FROM resources")
    void deleteAllResources();

    @Query("SELECT * FROM resources WHERE course_topic_id=:topicId AND type =:type LIMIT 5")
    LiveData<List<ResourceModel>> getFiveResourceByResourceId(int topicId, String type);
}
