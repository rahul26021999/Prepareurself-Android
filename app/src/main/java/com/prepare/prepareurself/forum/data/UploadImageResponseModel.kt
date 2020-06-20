package com.prepare.prepareurself.forum.data

data class UploadImageResponseModel(
        var error_code:Int?=0,
        var message:String?="",
        var image:String?="",
        var path:String?=""
)