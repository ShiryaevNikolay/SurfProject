package ru.shiryaev.surfproject.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class UserInfo(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("username")
    @Expose
    var username: String? = null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("userDescription")
    @Expose
    var userDescription: String? = null
)