package com.prepare.prepareurself.forum.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prepare.prepareurself.Apiservice.ApiClient
import com.prepare.prepareurself.Apiservice.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumRepository{

    private var apiInterface:ApiInterface?=null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }


    fun askQuery(token:String,courseId:Int,query:String):LiveData<ForumPostQueryResponseModel>{
        val data = MutableLiveData<ForumPostQueryResponseModel>()

        apiInterface?.askQuery(token, courseId, query)?.enqueue(object : Callback<ForumPostQueryResponseModel>{
            override fun onFailure(call: Call<ForumPostQueryResponseModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<ForumPostQueryResponseModel>, response: Response<ForumPostQueryResponseModel>) {
                val res = response.body()

                if (res!=null){
                    data.value = res
                }else{
                    data.value = null
                }

            }
        })

        return data

    }

}