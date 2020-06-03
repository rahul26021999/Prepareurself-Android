package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResponseModel(
        var id:Int?=0,
        var quizId:String?="",
        var isPaid:Boolean?=false,
        var isAvailable:Boolean?=false,
        var title:String?="",
        var created_at:String?="",
        var updated_at:String?=""
): Parcelable