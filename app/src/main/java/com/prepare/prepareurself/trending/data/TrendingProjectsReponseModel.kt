package com.prepare.prepareurself.trending.data

import com.prepare.prepareurself.courses.data.model.ProjectsModel

data class TrendingProjectsReponseModel(
        var success:Boolean?=false,
        var course_id:Int?=0,
        var projects:List<ProjectsModel>?=null
)