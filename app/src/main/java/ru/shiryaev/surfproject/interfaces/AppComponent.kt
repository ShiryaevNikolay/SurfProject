package ru.shiryaev.surfproject.interfaces

import dagger.Component
import ru.shiryaev.surfproject.dagger.StorageModule
import ru.shiryaev.surfproject.database.repository.AppRepository

@Component(modules = [StorageModule::class])
interface AppComponent {
    fun injectsAppRepository(appRepository: AppRepository)
}