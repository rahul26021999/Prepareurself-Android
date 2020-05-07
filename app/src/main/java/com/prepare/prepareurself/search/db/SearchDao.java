package com.prepare.prepareurself.search.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prepare.prepareurself.search.SearchModel;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSearchItem(SearchModel searchModel);

    @Query("SELECT * FROM searchitems")
    LiveData<List<SearchModel>> getSearchModels();

    @Query("DELETE FROM searchitems")
    void deleteSearchItems();

}
