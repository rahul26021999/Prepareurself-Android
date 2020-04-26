package com.prepare.prepareurself.dashboard.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.dashboard.data.model.BannerModel;

import java.util.List;

@Dao
public interface BannerImageRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBanner(BannerModel bannerModel);

    @Query("SELECT * FROM banner")
    LiveData<List<BannerModel>> getBanners();

    @Query("DELETE FROM banner")
    void deleteBanners();

}
