package com.prepare.prepareurself.Apiservice;

import com.prepare.prepareurself.utils.youtubeplaylistapi.models.YoutubePlaylistResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApiInterface {

    @GET("playlistItems")
    Call<YoutubePlaylistResponseModel> getPlaylist(@Query("part") String part,
                                                   @Query("pageToken") String pageToken,
                                                   @Query("playlistId") String playlistId,
                                                   @Query("key") String APIKEY);

}