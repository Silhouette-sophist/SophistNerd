package com.example.sophistnerd.api

import com.example.sophistnerd.data.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("order_by") orderBy: String = "relevant", //latest
        @Query("orientation") orientation: String = "landscape", //landscape, portrait, squarish
        @Query("client_id") clientId: String = "DaMXc7TjqqKRlN_pK95laDMWOX_gfdTJk7cSax26NBE"
    ): UnsplashResponse

    @GET("photos")
    suspend fun photoList(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("order_by") orderBy: String = "latest", //latest, oldest, popular
    )

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
    }
}