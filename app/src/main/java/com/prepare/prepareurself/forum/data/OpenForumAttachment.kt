package com.prepare.prepareurself.forum.data

data class OpenForumAttachment(
        var id:Int?=0,
        var reply_id:Int?=0,
        var query_id:Int?=0,
        var file:String?="",
        var created_at:String?="",
        var updated_at:String?=""
)