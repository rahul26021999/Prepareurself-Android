package com.prepare.prepareurself.utils.youtubeplaylistapi.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.utils.youtubeplaylistapi.models.SingleVIdeoItemWrapper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

@Dao
public interface SingleVideoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideoItemWrapper(SingleVIdeoItemWrapper itemWrapper);

    @Query("SELECT * FROM singlevideo WHERE id=:videoId")
    LiveData<SingleVIdeoItemWrapper> getVideoItemWrappers(String videoId);

    @Query("DELETE FROM singlevideo")
    void deleteVideoItemWrappers();

}
