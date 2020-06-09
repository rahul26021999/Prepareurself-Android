package com.prepare.prepareurself.preferences.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prepare.prepareurself.dashboard.data.db.repository.CourseDbRepository
import com.prepare.prepareurself.dashboard.data.model.CourseModel
import com.prepare.prepareurself.preferences.data.PrefDbRepository
import com.prepare.prepareurself.preferences.data.PrefRepository
import com.prepare.prepareurself.preferences.data.PreferencesModel
import com.prepare.prepareurself.profile.data.repository.ProfileRepository

class PreferenceViewModel(application: Application):AndroidViewModel(application){

    private var prefRepository:PrefRepository?=null
    private var predDbRepository:PrefDbRepository?=null
    private var courseDbRepository:CourseDbRepository?=null

    init {
        predDbRepository = PrefDbRepository(application)
        prefRepository = PrefRepository(application)
        courseDbRepository = CourseDbRepository(application)
    }

    fun fetchPreferences(token:String){
        prefRepository?.fetchPrefs(token)
    }

    fun getPrefs():LiveData<List<PreferencesModel>>?{
        return predDbRepository?.getPrefs()
    }

    fun insertPref(preferencesModel: PreferencesModel){
        predDbRepository?.insertPref(preferencesModel)
    }

    fun getCourses():LiveData<List<CourseModel>>?{
        return courseDbRepository?.allCourses
    }

    fun getUserPreferences(token:String):LiveData<List<PreferencesModel>>?{
        return prefRepository?.getUserPreferences(token)
    }

}