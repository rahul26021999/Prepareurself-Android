package com.prepare.prepareurself.preferences.data

data class PrefernceResponseModel(
        var error_code:Int?=-1,
        var preferences: List<PreferencesModel>?=null
)