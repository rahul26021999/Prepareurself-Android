package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizResponseModel(
        var error_code:Int?=0,
        var quiz:QuizResponseBody?=null
): Parcelable