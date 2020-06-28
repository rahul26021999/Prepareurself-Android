package com.prepare.prepareurself.trending.data

import com.prepare.prepareurself.courses.data.model.TopicsModel

data class TrendingTopicsResponseModel(
        var success:Boolean?=false,
        var course_id:Int?=0,
        var topics:List<TopicsModel>?=null
)