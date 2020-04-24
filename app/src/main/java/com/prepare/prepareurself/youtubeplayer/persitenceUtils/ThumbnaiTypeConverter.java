package com.prepare.prepareurself.youtubeplayer.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.Thumbails;

import java.lang.reflect.Type;

public class ThumbnaiTypeConverter {

    @TypeConverter
    public static Thumbails fromString(String value){
        Type type = new TypeToken<Thumbails>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromThumbnail(Thumbails thumbails){
        Gson gson = new Gson();
        return gson.toJson(thumbails);
    }

}
