package com.prepare.prepareurself.Apiservice;

import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubePlaylistResponseModel;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubeSingleVideoResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApiInterface {

    @GET("playlistItems")
    Call<YoutubePlaylistResponseModel> getPlaylist(@Query("part") String part,
                                                   @Query("pageToken") String pageToken,
                                                   @Query("playlistId") String playlistId,
                                                   @Query("key") String APIKEY);

    @GET("videos")
    Call<YoutubeSingleVideoResponseModel> getVideoDeatils(@Query("part") String part,
                                                          @Query("id") String videoId,
                                                          @Query("key") String APIKEY);

}
