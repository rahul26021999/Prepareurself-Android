package com.prepare.prepareurself.persistence.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;

import java.lang.reflect.Type;

public class CourseModelTypeCorveter {
    @TypeConverter
    public static CourseModel fromString(String value){
        Type type = new TypeToken<CourseModel>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromCourseModel(CourseModel courseModel){
        Gson gson = new Gson();
        return gson.toJson(courseModel);
    }
}
