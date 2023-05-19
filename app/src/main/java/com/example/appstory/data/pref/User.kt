package com.example.appstory.data.pref

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    @field:SerializedName("isLogin")
    var isLogin: Boolean? = false,

    @field:SerializedName("name")
    var name: String? = "",

    @field:SerializedName("userId")
    var userId: String? = "",

    @field:SerializedName("token")
    var token: String? = ""
) : Parcelable