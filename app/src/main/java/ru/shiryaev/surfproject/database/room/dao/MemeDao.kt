package ru.shiryaev.surfproject.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.shiryaev.surfproject.utils.MemeModel

@Dao
interface MemeDao {

    @Insert
    fun insert(meme: MemeModel)

    @Update
    fun update(meme: MemeModel)

    @Delete
    fun delete(meme: MemeModel)

    @Query("DELETE FROM meme_table")
    fun deleteAllMeme()

    @Query("SELECT * FROM meme_table")
    fun getAllMeme() : LiveData<List<MemeModel>>
}