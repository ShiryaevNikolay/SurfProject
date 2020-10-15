package ru.shiryaev.surfproject.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.shiryaev.surfproject.database.room.dao.MemeDao
import ru.shiryaev.surfproject.models.Meme

@Database(version = 1, entities = [Meme::class])
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun memeDao() : MemeDao

    companion object {
        private var instance: AppRoomDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AppRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppRoomDatabase::class.java, "meme_database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as AppRoomDatabase
        }
    }
}