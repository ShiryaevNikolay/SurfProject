package ru.shiryaev.surfproject.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("accessToken")
    @Expose
    var accessToken: String? = null,

    @SerializedName("userInfo")
    @Expose
    var userInfo: UserInfo? = null
)