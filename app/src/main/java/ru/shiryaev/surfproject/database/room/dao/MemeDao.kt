package ru.shiryaev.surfproject.database.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.shiryaev.surfproject.models.DbMeme

@Dao
interface MemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meme: DbMeme)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(listMeme: List<DbMeme>)

    @Update
    fun update(meme: DbMeme)

    @Delete
    fun delete(meme: DbMeme)

    @Query("DELETE FROM meme_table")
    fun deleteAllMeme()

    @Query("SELECT * FROM meme_table WHERE services = 'network'")
    fun getAllMeme() : LiveData<List<DbMeme>>

    @Query("SELECT * FROM meme_table WHERE services = :username")
    fun getAllMemeOfUser(username: String) : LiveData<List<DbMeme>>

    @Query("SELECT * FROM meme_table WHERE title LIKE :filter")
    fun getMemeOfFilter(filter: String) : LiveData<List<DbMeme>>
}