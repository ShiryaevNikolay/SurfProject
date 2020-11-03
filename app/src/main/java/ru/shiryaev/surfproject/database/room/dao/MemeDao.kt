package ru.shiryaev.surfproject.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.shiryaev.surfproject.models.DbMeme

@Dao
interface MemeDao {

    @Insert
    fun insert(meme: DbMeme)

    @Update
    fun update(meme: DbMeme)

    @Delete
    fun delete(meme: DbMeme)

    @Query("DELETE FROM meme_table")
    fun deleteAllMeme()

    @Query("SELECT * FROM meme_table")
    fun getAllMeme() : LiveData<List<DbMeme>>
}