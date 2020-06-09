package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OptionsModel(
        var id:Int?=0,
        var option:String?="",
        var image:String?="",
        var question_id:String?="",
        var created_at:String?="",
        var updated_at:String?=""
): Parcelable