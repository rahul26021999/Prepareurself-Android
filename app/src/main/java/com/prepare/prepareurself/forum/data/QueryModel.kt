package com.prepare.prepareurself.forum.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QueryModel(
    var id:Int=0,
    var user_id:Int?=0,
    var course_id:Int?=0,
    var query_id:Int?=0,
    var clap:Int?=0,
    var total_claps:Int?=0,
    var query:String?="",
    var reply:String?="",
    var image:String?="",
    var created_at:String?="",
    var updated_at:String?="",
    var user:ForumUser?=null,
    var open_forum_attachment:List<OpenForumAttachment>?=null
):Parcelable