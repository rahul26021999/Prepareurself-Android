package com.prepare.prepareurself.favourites.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;

import java.util.List;

@Dao
public interface LikedResourcesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertResource(LikedResourcesModel resourceModel);

    @Query("SELECT * FROM likedresources")
    LiveData<List<LikedResourcesModel>> getAllResources();

    @Query("SELECT * FROM likedresources WHERE course_topic_id=:course_topic_id AND type =:type")
    LiveData<List<LikedResourcesModel>> getResourcesByID(int course_topic_id, String type);

    @Query("SELECT * FROM likedresources WHERE type =:type AND id != :resource_id")
    LiveData<List<LikedResourcesModel>> getResourceByIdExcept(String type, int resource_id);

    @Query("SELECT * FROM likedresources WHERE id=:resourceId AND type=:type")
    LiveData<LikedResourcesModel> getResourceByResourceId(int resourceId, String type);

    @Query("DELETE FROM likedresources")
    void deleteAllResources();

    @Query("SELECT * FROM likedresources WHERE course_topic_id=:topicId AND type =:type LIMIT 5")
    LiveData<List<LikedResourcesModel>> getFiveResourceByResourceId(int topicId, String type);

}
