package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class InstructionsModel(
        var _id:String?="",
        var instruction:String?=""
): Parcelable