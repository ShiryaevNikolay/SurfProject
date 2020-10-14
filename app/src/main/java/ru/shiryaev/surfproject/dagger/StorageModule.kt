package ru.shiryaev.surfproject.dagger

import dagger.Module
import dagger.Provides
import ru.shiryaev.surfproject.database.room.AppRoomDatabase
import ru.shiryaev.surfproject.utils.App

@Module
class StorageModule {
    @Provides
    fun provideAppRoomDatabase() = AppRoomDatabase.getInstance(App)
}