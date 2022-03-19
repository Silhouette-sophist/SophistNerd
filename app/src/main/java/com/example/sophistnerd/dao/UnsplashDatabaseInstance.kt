package com.example.sophistnerd.dao

import androidx.room.Room
import com.example.sophistnerd.SophistApplication

/**
 * 数据库实例，单例，通过惰性初始化
 */
object UnsplashDatabaseInstance {

    val unsplashDatabase by lazy {
        Room.databaseBuilder(
            SophistApplication.sContext,
            UnsplashSearchDatabase::class.java,
            "unsplash_operate_info"
        ).build()
    }
}