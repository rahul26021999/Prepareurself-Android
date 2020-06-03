package com.prepare.prepareurself.preferences.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pref")
data class PreferencesModel(
        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        var id:Int?=0,

        @ColumnInfo(name = "name")
        @SerializedName("name")
        var name:String?=""
)