package com.prepare.prepareurself.forum.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prepare.prepareurself.Apiservice.ApiClient
import com.prepare.prepareurself.Apiservice.ApiInterface
import com.prepare.prepareurself.profile.data.model.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumRepository{

    private var apiInterface:ApiInterface?=null
    var queriesLiveData : MutableLiveData<GetQueriesResponseModel>?=null
    var repliesLiveData : MutableLiveData<GetQueriesResponseModel>?=null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
        queriesLiveData = MutableLiveData()
        repliesLiveData = MutableLiveData()
    }


    fun askQuery(token:String,courseId:Int,query:String, imageList:ArrayList<String>):LiveData<ForumPostQueryResponseModel>{
        val data = MutableLiveData<ForumPostQueryResponseModel>()

        apiInterface?.askQuery(token, courseId, query, imageList)?.enqueue(object : Callback<ForumPostQueryResponseModel>{
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

    fun getQueries(token:String,courseId:Int,page:Int):LiveData<GetQueriesResponseModel>?{

        apiInterface?.getQueries(token, courseId, 10,page)?.enqueue(object : Callback<GetQueriesResponseModel>{
            override fun onFailure(call: Call<GetQueriesResponseModel>, t: Throwable) {
                queriesLiveData?.value = null
            }

            override fun onResponse(call: Call<GetQueriesResponseModel>, response: Response<GetQueriesResponseModel>) {
                val res = response.body()

                if (res!=null){
                    queriesLiveData?.value = res
                }else{
                    queriesLiveData?.value = null
                }

            }
        })

        return queriesLiveData

    }

    fun getReplies(token:String,queryId:Int,page:Int):LiveData<GetQueriesResponseModel>?{

        apiInterface?.getReplies(token, queryId, 10,page)?.enqueue(object : Callback<GetQueriesResponseModel>{
            override fun onFailure(call: Call<GetQueriesResponseModel>, t: Throwable) {
                repliesLiveData?.value = null
            }

            override fun onResponse(call: Call<GetQueriesResponseModel>, response: Response<GetQueriesResponseModel>) {
                val res = response.body()

                if (res!=null){
                    repliesLiveData?.value = res
                }else{
                    repliesLiveData?.value = null
                }

            }
        })

        return repliesLiveData

    }

    fun doClap(token: String, replyId:Int, status:Int):LiveData<DoClapModel>{
        val data = MutableLiveData<DoClapModel>()

        Log.d("clap_debug_url","${apiInterface?.doClap(token, replyId, status)?.request()?.url()}")

        apiInterface?.doClap(token, replyId, status)?.enqueue(object : Callback<DoClapModel>{
            override fun onFailure(call: Call<DoClapModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<DoClapModel>, response: Response<DoClapModel>) {
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

    fun doReply(token:String,queryId:Int,reply:String, images:ArrayList<String>):LiveData<DoReplyResponseModel>{
        val data = MutableLiveData<DoReplyResponseModel>()


        apiInterface?.doReply(token, queryId,reply, images)?.enqueue(object : Callback<DoReplyResponseModel>{
            override fun onFailure(call: Call<DoReplyResponseModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<DoReplyResponseModel>, response: Response<DoReplyResponseModel>) {
                val res = response.body()

                if (res!=null && res.error_code == 0){
                    data.value = res
                }else{
                    data.value = null
                }

            }
        })

        return data

    }

    fun uploadImage(token: String, type:Int, body: MultipartBody.Part):LiveData<UploadImageResponseModel>{
        val data = MutableLiveData<UploadImageResponseModel>()

        Log.d("upload_image_query","${apiInterface?.uploadQueryImage(token,type,body)?.request()?.url()}")

        apiInterface?.uploadQueryImage(token,type,body)?.enqueue(object : Callback<UploadImageResponseModel>{
            override fun onFailure(call: Call<UploadImageResponseModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<UploadImageResponseModel>, response: Response<UploadImageResponseModel>) {
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