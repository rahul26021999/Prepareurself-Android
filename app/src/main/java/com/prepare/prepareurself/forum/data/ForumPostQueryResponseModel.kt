package com.prepare.prepareurself.forum.data

data class ForumPostQueryResponseModel(
    var query:QueryModel?=null,
    var error_code:Int?=0,
    var message:String?=""
)