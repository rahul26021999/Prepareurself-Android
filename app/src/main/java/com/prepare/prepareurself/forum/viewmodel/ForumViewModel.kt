package com.prepare.prepareurself.forum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prepare.prepareurself.forum.data.*
import com.prepare.prepareurself.profile.data.model.UploadImageResponse
import okhttp3.MultipartBody

class ForumViewModel(application: Application) : AndroidViewModel(application){

    private var forumRepository:ForumRepository?=null

    init {
        forumRepository = ForumRepository()
    }

    fun askQuery(token:String, courseId:Int, query:String,imageList:ArrayList<String>):LiveData<ForumPostQueryResponseModel>?{
        return forumRepository?.askQuery(token, courseId, query, imageList)
    }

    fun doReply(token: String,queryId:Int,reply:String, images:ArrayList<String>):LiveData<DoReplyResponseModel>?{
        return forumRepository?.doReply(token, queryId, reply, images)
    }

    fun getRelies(token: String,queryId:Int,page:Int):LiveData<GetQueriesResponseModel>?{
        return forumRepository?.getReplies(token, queryId, page)
    }

    fun getQueries(token:String, courseId:Int, page: Int):LiveData<GetQueriesResponseModel>?{
        return forumRepository?.getQueries(token,courseId,page)
    }

    fun uploadQueryImage(token: String, type:Int, body:MultipartBody.Part): LiveData<UploadImageResponseModel>? {
        return forumRepository?.uploadImage(token, type, body)
    }


}