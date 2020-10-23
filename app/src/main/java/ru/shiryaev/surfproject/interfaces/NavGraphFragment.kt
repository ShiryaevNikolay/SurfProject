package ru.shiryaev.surfproject.interfaces

interface NavGraphFragment {
    fun startLoginScreenFragmentFromSplashScreenFragment()
    fun startMainScreenFragmentFromLoginScreenFragment()
    fun startMainScreenFragmentFromSplashScreenFragment()
    fun startLoginScreenFragmentFromMainScreenFragment()
}