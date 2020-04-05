package com.example.prepareurself.persistence.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prepareurself.persistence.model.UserRoomModel;

@Dao
public interface UserRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserRoomModel userRoomModel);

    @Query("SELECT * from user_info")
    LiveData<UserRoomModel> getUserInfo();

    @Query("DELETE FROM user_info")
    void deleteAll();

}
