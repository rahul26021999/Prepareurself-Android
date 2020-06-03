package com.prepare.prepareurself.preferences.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrefModel(preferencesModel: PreferencesModel)

    @Query("SELECT * FROM pref")
    fun getPrefs() : LiveData<List<PreferencesModel>>

    @Query("DELETE FROM pref WHERE id=:id")
    fun deletePrefModel(id:Int)

    @Query("DELETE FROM pref")
    fun deleteAllPrefs()

}