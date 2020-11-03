package ru.shiryaev.surfproject.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkMeme(
    @SerializedName("createdDate")
    @Expose
    val createdDate: Long? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("isFavorite")
    @Expose
    val isFavorite: Boolean? = null,

    @SerializedName("photoUrl")
    @Expose
    val photoUrl: String? = null,

    @SerializedName("title")
    @Expose
    val title: String? = null
)