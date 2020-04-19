package com.prepare.prepareurself.utils.youtubeplaylistapi.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;

import java.util.List;

@Dao
public interface PlaylistVideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideoContent(VideoContentDetails videoContentDetails);

    @Query("SELECT * FROM playlistVideos")
    LiveData<List<VideoContentDetails>> getVideoContentDetails();

    @Query("DELETE FROM playlistVideos")
    void deleteVideoContentDetails();


}
