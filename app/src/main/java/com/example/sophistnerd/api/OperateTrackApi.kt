package com.example.sophistnerd.api

import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import retrofit2.http.Field
import retrofit2.http.POST

interface OperateTrackApi {

    @POST("api/upload/search")
    fun uploadSearchKeywords(
        @Field("keywords") keywords: String,
        @Field("search_results") results: List<UnsplashPhoto>
    )

    @POST("api/upload/download")
    fun uploadDownloadImage(
        @Field("unsplash_photo") unsplashPhoto: UnsplashPhoto,
        @Field("image_type") type: String
    )

    companion object {
        const val BASE_URL = "http://localhost:8080"
    }
}