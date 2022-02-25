package com.example.sophistnerd

import android.app.Application
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SophistApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        UnsplashPhotoPicker.init(this,
        "DaMXc7TjqqKRlN_pK95laDMWOX_gfdTJk7cSax26NBE",
        "NzPXWOq8DGq3b_BBP2gvaydk6DDJQXPGH_MAh_w9nI0")
    }
}