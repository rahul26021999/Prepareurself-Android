package com.prepare.prepareurself.forum.data

data class DoReplyResponseModel(
        var reply:QueryModel?=null,
        var error_code:Int?=0,
        var message:String?=""
)