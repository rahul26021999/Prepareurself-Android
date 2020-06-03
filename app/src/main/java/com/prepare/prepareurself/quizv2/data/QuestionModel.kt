package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionModel(
        var questionImage:String?="",
        var type:String?="",
        var _id:String?="",
        var question:String?="",
        var options:List<OptionsModel>?=null

): Parcelable