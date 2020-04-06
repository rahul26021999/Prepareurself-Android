package com.example.prepareurself.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.example.prepareurself.authentication.data.model.UserModel;

@Database(entities = {UserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRoomDao userRoomDao();

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
