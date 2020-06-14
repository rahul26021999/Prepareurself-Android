package com.prepare.prepareurself.forum.data

data class QueryModel(
    var id:Int=0,
    var user_id:Int?=0,
    var course_id:Int?=0,
    var query:String?="",
    var reply:String?="",
    var image:String?="",
    var created_at:String?="",
    var updated_at:String?=""
)