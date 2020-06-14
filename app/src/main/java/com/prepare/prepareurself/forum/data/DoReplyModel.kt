package com.prepare.prepareurself.forum.data

data class DoReplyModel(
        var id:Int?=0,
        var user_id:Int?=0,
        var query_id:Int?=0,
        var reply:String?="",
        var created_at:String?="",
        var updated_at:String?=""
)