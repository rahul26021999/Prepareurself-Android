package com.prepare.prepareurself.forum.data

data class QueriesModel(
        var current_page:Int?=0,
        var data:List<QueryModel>?=null,
        var first_page_url:String?="",
        var from:Int?=0,
        var last_page:Int?=0,
        var last_page_url:String?="",
        var next_page_url:String?="",
        var  path:String?="",
        var per_page:Int?=0,
        var prev_page_url:String?="",
        var to:Int?=0,
        var total:Int?=0
)