package com.example.sophistnerd.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sophistnerd.data.LocalKeywordsSearchInfo

@Database(entities = [LocalKeywordsSearchInfo::class], version = 1, exportSchema = false)
abstract class UnsplashSearchDatabase : RoomDatabase() {

    abstract fun unsplashSearchInfoDao() : UnsplashSearchInfoDao
}