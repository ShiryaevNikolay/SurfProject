package ru.shiryaev.surfproject.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meme(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("title")
    @Expose
    val title: String? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("isFavorite")
    @Expose
    val isFavorite: Boolean? = null,

    @SerializedName("createdDate")
    @Expose
    val createdDate: Int? = null,

    @SerializedName("photoUrl")
    @Expose
    val photoUrl: String? = null
)