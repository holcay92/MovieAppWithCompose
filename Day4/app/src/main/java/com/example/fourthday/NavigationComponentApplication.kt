package com.example.fourthday

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NavigationComponentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}