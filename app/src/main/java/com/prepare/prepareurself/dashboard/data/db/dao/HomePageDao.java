package com.prepare.prepareurself.dashboard.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.dashboard.data.model.HomepageData;

import java.util.List;

@Dao
public interface HomePageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHomePageData(HomepageData homepageData);

    @Query("SELECT * FROM home_page")
    LiveData<List<HomepageData>> getHomePageData();

    @Query("DELETE FROM home_page")
    void deleteHomePageData();

}
