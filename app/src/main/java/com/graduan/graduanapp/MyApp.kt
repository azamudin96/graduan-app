package com.graduan.graduanapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApp : Application() {

    companion object {
        lateinit var appContext: Context
        var sharedPreferences: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        appContext = this
    }
}