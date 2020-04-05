package com.example.prepareurself.persistence.repository;

import android.content.Context;
import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.prepareurself.persistence.model.UserRoomModel;

@Database(entities = {UserRoomModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRoomDao userRoomDao();

    private static AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

}
