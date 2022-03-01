package com.example.sophistnerd.data

import android.graphics.Bitmap
import android.os.Parcelable
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.android.parcel.Parcelize

/**
 * 对数据源进行封装状态，如果bitmap已经下载过就直接使用，否则进行下载
 */
@Parcelize
data class UnsplashPhotoWithStatus(
    val unsplashPhoto: UnsplashPhoto,
    var bitmap: Bitmap? = null) : Parcelable
