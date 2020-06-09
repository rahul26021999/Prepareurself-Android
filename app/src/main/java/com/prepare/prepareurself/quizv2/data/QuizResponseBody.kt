package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResponseBody(
        var id:Int?=0,
        var user_id:Int?=0,
        var status:String?="",
        var created_at:String?="",
        var updated_at:String?="",
        var questions:List<QuestionModel>?=null
): Parcelable