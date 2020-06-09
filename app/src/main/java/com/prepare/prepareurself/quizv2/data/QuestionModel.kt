package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionModel(
        var id:Int?=0,
        var question:String?="",
        var course_id:Int?=0,
        var ques_level:String?="",
        var created_at:String?="",
        var updated_at:String?="",
        var option:List<OptionsModel>?=null,
        var answer:AnswerModel?=null

): Parcelable