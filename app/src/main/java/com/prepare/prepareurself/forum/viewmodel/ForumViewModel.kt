package com.prepare.prepareurself.forum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prepare.prepareurself.forum.data.ForumPostQueryResponseModel
import com.prepare.prepareurself.forum.data.ForumRepository

class ForumViewModel(application: Application) : AndroidViewModel(application){

    private var forumRepository:ForumRepository?=null

    init {
        forumRepository = ForumRepository()
    }

    fun askQuery(token:String, courseId:Int, query:String):LiveData<ForumPostQueryResponseModel>?{
        return forumRepository?.askQuery(token, courseId, query)
    }

}