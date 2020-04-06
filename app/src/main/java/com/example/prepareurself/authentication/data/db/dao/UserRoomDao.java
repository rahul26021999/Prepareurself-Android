package com.example.prepareurself.authentication.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prepareurself.authentication.data.model.UserModel;

@Dao
public interface UserRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserModel userModel);

    @Query("SELECT * from user_info")
    LiveData<UserModel> getUserInfo();

    @Query("DELETE FROM user_info")
    void deleteAll();

}
