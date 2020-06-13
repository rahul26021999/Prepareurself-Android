package com.prepare.prepareurself.preferences.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.prepare.prepareurself.authentication.data.db.dao.UserRoomDao
import com.prepare.prepareurself.authentication.data.model.UserModel
import com.prepare.prepareurself.persistence.AppDatabase

class PrefDbRepository(application: Application){

    private var prefDao:PrefDao?=null

    init {
        val db = AppDatabase.getDatabase(application)
        prefDao = db.prefDao()
    }

    fun insertPref(preferencesModel: PreferencesModel){
        prefDao?.let { InsertAsyncTask(it).execute(preferencesModel) }
    }

    fun deletePref(preferencesModel: PreferencesModel){
        prefDao?.let { DeleteAsyncTask(it).execute(preferencesModel) }
    }

    fun deleteAllPrefs(){
        prefDao?.let{ DeleteAllAsyncTask(it).execute() }
    }

    fun getPrefs():LiveData<List<PreferencesModel>>? {
        return prefDao?.getPrefs()
    }

    class InsertAsyncTask(var dao: PrefDao) : AsyncTask<PreferencesModel?, Void?, Void?>() {

        override fun doInBackground(vararg params: PreferencesModel?): Void? {
            params[0]?.let { dao.insertPrefModel(it) }
            return null
        }

    }

    class DeleteAsyncTask(var dao: PrefDao) : AsyncTask<PreferencesModel?, Void?, Void?>() {

        override fun doInBackground(vararg params: PreferencesModel?): Void? {
            params[0]?.let { it.id?.let { it1 -> dao.deletePrefModel(it1) } }
            return null
        }

    }

    class DeleteAllAsyncTask(var dao: PrefDao) : AsyncTask<PreferencesModel?, Void?, Void?>() {

        override fun doInBackground(vararg params: PreferencesModel?): Void? {
            dao.deleteAllPrefs()
            return null
        }

    }

}