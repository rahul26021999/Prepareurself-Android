package com.example.prepareurself.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prepareurself.Home.content.dashboard.data.db.dao.CourseRoomDao;
import com.example.prepareurself.Home.content.dashboard.data.model.CourseModel;
import com.example.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.example.prepareurself.authentication.data.model.UserModel;

@Database(entities = {UserModel.class, CourseModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRoomDao userRoomDao();
    public abstract CourseRoomDao courseRoomDao();

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
