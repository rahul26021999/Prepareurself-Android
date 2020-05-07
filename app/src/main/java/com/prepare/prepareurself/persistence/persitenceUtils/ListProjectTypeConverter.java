package com.prepare.prepareurself.persistence.persitenceUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceModel;

import java.lang.reflect.Type;
import java.util.List;

public class ListProjectTypeConverter {

    @TypeConverter
    public static List<ProjectsModel> fromString(String value){
        Type type = new TypeToken<List<ProjectsModel>>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromListProjects(List<ProjectsModel> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
