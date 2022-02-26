package com.example.sophistnerd

import android.app.Application
import android.content.Context
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker

class SophistApplication : Application() {

    companion object {
        lateinit var sContext : Context
    }

    override fun onCreate() {
        super.onCreate()

        sContext = this

        UnsplashPhotoPicker.init(this,
        "DaMXc7TjqqKRlN_pK95laDMWOX_gfdTJk7cSax26NBE",
        "NzPXWOq8DGq3b_BBP2gvaydk6DDJQXPGH_MAh_w9nI0")
    }
}