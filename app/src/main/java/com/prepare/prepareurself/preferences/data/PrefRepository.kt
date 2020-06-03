package com.prepare.prepareurself.preferences.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.prepare.prepareurself.Apiservice.ApiClient
import com.prepare.prepareurself.Apiservice.ApiInterface
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrefRepository(var application: Application) {

    private var apiInterface:ApiInterface?=null
    private var prefDbRepository:PrefDbRepository?=null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
        prefDbRepository = PrefDbRepository(application)
    }

    fun fetchPrefs(token:String){
        apiInterface?.getPreferences(token)?.enqueue(object : Callback<PrefernceResponseModel>{
            override fun onFailure(call: Call<PrefernceResponseModel>, t: Throwable) {
                Utility.showToast(application,Constants.SOMETHINGWENTWRONG)
            }

            override fun onResponse(call: Call<PrefernceResponseModel>, response: Response<PrefernceResponseModel>) {

                val res = response.body()
                if (res!=null && res.error_code == 0){
                    res.preferences?.forEach {
                        prefDbRepository?.insertPref(it)
                    }
                }else{
                    Utility.showToast(application,Constants.SOMETHINGWENTWRONG)
                }
            }
        })
    }

}