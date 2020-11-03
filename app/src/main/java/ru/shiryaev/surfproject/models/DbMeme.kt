package ru.shiryaev.surfproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meme_table")
class DbMeme  {
    @ColumnInfo(name = "createdDate")
    var createdDate: Long? = null
    @ColumnInfo(name = "description")
    var description: String? = null
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = null
    @ColumnInfo(name = "photoUrl")
    var photoUrl: String? = null
    @ColumnInfo(name = "title")
    var title: String? = null
}