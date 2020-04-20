package com.prepare.prepareurself.utils.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;

import java.lang.reflect.Type;

public class VideoContentDetailsConverter {

    @TypeConverter
    public static VideoContentDetails fromString(String value){
        Type type = new TypeToken<VideoContentDetails>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromVideoItemWrapper(VideoContentDetails videoContentDetails){
        Gson gson = new Gson();
        return gson.toJson(videoContentDetails);
    }

}
