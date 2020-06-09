package com.prepare.prepareurself.quizv2.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerModel(
      var id:Int?=0,
      var question_id:Int?=0,
      var option_id:Int?=0,
      var created_at:String?="",
      var updated_at:String?=""
):Parcelable