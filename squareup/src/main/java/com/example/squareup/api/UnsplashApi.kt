package com.example.squareup.api

import com.example.squareup.UnsplashData
import retrofit2.http.*

interface UnsplashApi {

    @POST("/api/post/complex")
    suspend fun postComplexInfo(@Body unsplashData: UnsplashData) : String

    @GET("/api/post/base")
    suspend fun postBaseInfo(@Query("name") name: String) : String

    companion object {
        const val BASE_URL = "http://192.168.1.124:8081"
    }
}