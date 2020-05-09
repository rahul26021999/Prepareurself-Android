package com.prepare.prepareurself.search.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.persistence.AppDatabase;
import com.prepare.prepareurself.search.models.SearchModel;

import java.util.List;

public class SearchDbRespository {

    private SearchDao searchDao;
    private LiveData<List<SearchModel>> searchLiveData;

    public SearchDbRespository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        searchDao = db.searchDao();
    }

    public LiveData<List<SearchModel>> getSearchItems(){
        return searchDao.getSearchModels();
    }

    public void insertSearch(SearchModel searchModel){
        new insertAsyncTask(searchDao).execute(searchModel);
    }

    public void deleteAllSearches(){
        new deleteAllAsyncTask(searchDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<SearchModel,Void, Void> {

        private SearchDao dao;

        insertAsyncTask(SearchDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SearchModel... searchModels) {
            dao.insertSearchItem(searchModels[0]);

            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<SearchModel,Void, Void>{

        private SearchDao dao;

        deleteAllAsyncTask(SearchDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SearchModel... searchModels) {
            dao.deleteSearchItems();

            return null;
        }
    }

}
