package com.prepare.prepareurself.utils.youtubeplaylistapi.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

@Dao
public interface PlaylistContentDeatilsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideoItemWrapper(VideoItemWrapper itemWrapper);

    @Query("SELECT * FROM playlistItemWrapper")
    LiveData<List<VideoItemWrapper>> getVideoItemWrappers();

    @Query("SELECT * FROM playlistItemWrapper WHERE snippet_playlistId=:playlistId")
    LiveData<List<VideoItemWrapper>> getVideoItemWrapperByPlaylistId(String playlistId);

    @Query("SELECT * FROM playlistItemWrapper WHERE content_videoId=:videoCode AND snippet_playlistId=:playlistId")
    LiveData<VideoItemWrapper> getVideoItemWrapperByVideoCodeAndPlaylistId(String videoCode, String playlistId);

    @Query("DELETE FROM playlistItemWrapper")
    void deleteVideoItemWrappers();


}
