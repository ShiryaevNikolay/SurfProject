package ru.shiryaev.surfproject.utils

import android.app.Application
import ru.shiryaev.surfproject.interfaces.AppComponent
import ru.shiryaev.surfproject.interfaces.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.create()
    }

    companion object {
        const val LOADING_MEME = "LOADING_MEME"
        const val REFRESH_MEME = "REFRESH_MEME"

        private lateinit var component: AppComponent
        private lateinit var instance: App

        fun getComponent() = component

        fun getInstance() = instance
    }
}