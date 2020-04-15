package com.prepare.prepareurself.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.prepare.prepareurself.Home.content.courses.data.db.dao.TopicsRoomDao;
import com.prepare.prepareurself.Home.content.courses.data.model.TopicsModel;
import com.prepare.prepareurself.Home.content.dashboard.data.db.dao.CourseRoomDao;
import com.prepare.prepareurself.Home.content.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.Home.content.resources.data.db.dao.ResourcesRoomDao;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceModel;
import com.prepare.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.prepare.prepareurself.authentication.data.model.UserModel;

@Database(entities = {
        UserModel.class,
        CourseModel.class,
        TopicsModel.class,
        ResourceModel.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRoomDao userRoomDao();
    public abstract CourseRoomDao courseRoomDao();
    public abstract TopicsRoomDao topicsRoomDao();
    public abstract ResourcesRoomDao resourcesRoomDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"app_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }

}
