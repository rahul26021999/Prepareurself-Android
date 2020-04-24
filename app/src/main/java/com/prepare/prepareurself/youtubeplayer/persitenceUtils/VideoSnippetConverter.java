package com.prepare.prepareurself.youtubeplayer.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoSnippet;

import java.lang.reflect.Type;

public class VideoSnippetConverter {

    @TypeConverter
    public static VideoSnippet fromString(String value){
        Type type = new TypeToken<VideoSnippet>(){}.getType();
        return new Gson().fromJson(value,type);
    }

    @TypeConverter
    public static String fromVideoSnippet(VideoSnippet videoSnippet){
        Gson gson = new Gson();
        return gson.toJson(videoSnippet);
    }

}
