package com.prepare.prepareurself.profile.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;

import java.util.List;

@Dao
public interface MyPreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreference(MyPreferenceTechStack preferredTechStack);

    @Query("SELECT * FROM myprereference")
    LiveData<List<MyPreferenceTechStack>> getPreferences();

    @Query("DELETE FROM myprereference")
    void deleteAllPreferences();

}
