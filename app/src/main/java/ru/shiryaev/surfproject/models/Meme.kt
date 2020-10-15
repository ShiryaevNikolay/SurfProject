package ru.shiryaev.surfproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "meme_table")
data class Meme(
    @SerializedName("createdDate")
    @Expose
    @PrimaryKey
    val createdDate: Int? = null,

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
    val title: String? = null,
)