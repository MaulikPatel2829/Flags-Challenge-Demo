package com.example.flagschallenge

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {
    lateinit var appContext: Context
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}