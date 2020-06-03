package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResponseBody(
        var duration:Int?=0,
        var questions:List<QuestionModel>?=null,
        var type:String?="",
        var title:String?="",
        var description:String?="",
        var instructions:List<InstructionsModel>?=null
): Parcelable