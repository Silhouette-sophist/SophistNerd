package com.example.sophistnerd.api

import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import retrofit2.http.*

interface OperateTrackApi {

    /**
     * Field，是form-data里面的
     * Query，是会拼接到url里面的
     * Body，是会以json传递body数据
     */
    @POST("api/upload/search")
    suspend fun uploadSearchKeywords(
        @Query("keywords") keywords: String,
        @Body search_results: List<UnsplashPhoto>
    ) : String

    @POST("api/upload/download")
    suspend fun uploadDownloadImage(
        @Query("image_type") type: String,
        @Body unsplash_photo: UnsplashPhoto
    ) : String

    companion object {
        const val BASE_URL = "http://192.168.1.119:8080"
    }
}