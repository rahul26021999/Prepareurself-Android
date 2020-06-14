package com.prepare.prepareurself.forum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prepare.prepareurself.forum.data.DoReplyResponseModel
import com.prepare.prepareurself.forum.data.ForumPostQueryResponseModel
import com.prepare.prepareurself.forum.data.ForumRepository
import com.prepare.prepareurself.forum.data.GetQueriesResponseModel

class ForumViewModel(application: Application) : AndroidViewModel(application){

    private var forumRepository:ForumRepository?=null

    init {
        forumRepository = ForumRepository()
    }

    fun askQuery(token:String, courseId:Int, query:String):LiveData<ForumPostQueryResponseModel>?{
        return forumRepository?.askQuery(token, courseId, query)
    }

    fun doReply(token: String,queryId:Int,reply:String):LiveData<DoReplyResponseModel>?{
        return forumRepository?.doReply(token, queryId, reply)
    }

    fun getRelies(token: String,queryId:Int,page:Int):LiveData<GetQueriesResponseModel>?{
        return forumRepository?.getReplies(token, queryId, page)
    }

    fun getQueries(token:String, courseId:Int, page: Int):LiveData<GetQueriesResponseModel>?{
        return forumRepository?.getQueries(token,courseId,page)
    }


}