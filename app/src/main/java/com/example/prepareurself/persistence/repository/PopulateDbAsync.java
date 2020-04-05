package com.example.prepareurself.persistence.repository;

import android.os.AsyncTask;

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
