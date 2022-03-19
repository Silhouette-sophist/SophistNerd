package com.example.sophistnerd.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sophistnerd.data.LocalKeywordsSearchInfo

@Dao
interface UnsplashSearchInfoDao {

    @Query("SELECT * FROM keywords_search_info_table")
    fun searchAll() : List<LocalKeywordsSearchInfo>

    @Query("SELECT * FROM keywords_search_info_table WHERE search_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<LocalKeywordsSearchInfo>

    @Insert
    fun insertAll(vararg keywordsSearchInfo: LocalKeywordsSearchInfo)

    @Delete
    fun delete(keywordsSearchInfo: LocalKeywordsSearchInfo)
}