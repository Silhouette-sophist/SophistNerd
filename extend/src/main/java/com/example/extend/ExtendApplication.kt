package com.example.extend

import android.app.Application

class ExtendApplication : Application() {

    companion object {
        var sApplication : Application? = null
    }

    override fun onCreate() {
        super.onCreate()

        sApplication = this
    }
}