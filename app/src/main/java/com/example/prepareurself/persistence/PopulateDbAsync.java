package com.example.prepareurself.persistence;

import android.os.AsyncTask;

import com.example.prepareurself.authentication.data.db.dao.UserRoomDao;

public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final UserRoomDao userRoomDao;

    PopulateDbAsync(AppDatabase database){
        userRoomDao = database.userRoomDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        userRoomDao.deleteAll();

        return null;
    }
}
