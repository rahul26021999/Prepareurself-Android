package com.prepare.prepareurself.profile.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.profile.data.model.PreferredTechStack;

import java.util.List;

@Dao
public interface UserPrefernceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreference(PreferredTechStack preferredTechStack);

    @Query("SELECT * FROM preferredtechstack")
    LiveData<List<PreferredTechStack>> getPreferences();

    @Query("DELETE FROM preferredtechstack")
    void deleteAllPreferences();

}
