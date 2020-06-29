package com.prepare.prepareurself.quizv2.data

data class QuizAnswerResponse(
        var error_code:Int?=0,
        var message:String?="",
        var score: Int?=0
)