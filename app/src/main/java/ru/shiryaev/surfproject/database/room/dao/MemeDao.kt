package ru.shiryaev.surfproject.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.shiryaev.surfproject.models.Meme

@Dao
interface MemeDao {

    @Insert
    fun insert(meme: Meme)

    @Update
    fun update(meme: Meme)

    @Delete
    fun delete(meme: Meme)

    @Query("DELETE FROM meme_table")
    fun deleteAllMeme()

    @Query("SELECT * FROM meme_table")
    fun getAllMeme() : LiveData<List<Meme>>
}