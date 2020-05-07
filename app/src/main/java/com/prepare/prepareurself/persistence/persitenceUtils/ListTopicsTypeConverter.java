package com.prepare.prepareurself.persistence.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.courses.data.model.TopicsModel;

import java.lang.reflect.Type;
import java.util.List;

public class ListTopicsTypeConverter {

    @TypeConverter
    public static List<TopicsModel> fromString(String value){
        Type type = new TypeToken<List<TopicsModel>>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromListTopics(List<TopicsModel> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
