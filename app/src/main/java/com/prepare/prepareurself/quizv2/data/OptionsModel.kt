package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OptionsModel(
        var optionImage:String?="",
        var _id:String?="",
        var option:String?=""
): Parcelable