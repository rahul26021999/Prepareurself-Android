package com.prepare.prepareurself.forum.data

data class GetQueriesResponseModel(
        var queries:QueriesModel?=null,
        var query:QueriesModel?=null,
        var error_code:Int?=0,
        var message:String?=""
)