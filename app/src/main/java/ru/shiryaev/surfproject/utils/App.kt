package ru.shiryaev.surfproject.utils

import android.app.Application
import ru.shiryaev.surfproject.interfaces.AppComponent
import ru.shiryaev.surfproject.interfaces.DaggerAppComponent

object App : Application() {

    private var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }

    fun getComponent() = component
}