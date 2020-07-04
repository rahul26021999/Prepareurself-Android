package com.prepare.prepareurself.forum.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForumUser(
        var first_name:String?="",
        var last_name:String?="",
        var username:String?="",
        var id:Int?=0,
        var profile_image:String?=""
) : Parcelable