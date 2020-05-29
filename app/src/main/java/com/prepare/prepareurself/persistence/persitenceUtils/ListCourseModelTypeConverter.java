package com.prepare.prepareurself.persistence.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;

import java.lang.reflect.Type;
import java.util.List;

public class ListCourseModelTypeConverter {

    @TypeConverter
    public static List<CourseModel> fromString(String value){
        Type type = new TypeToken<List<CourseModel>>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromListCourses(List<CourseModel> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
