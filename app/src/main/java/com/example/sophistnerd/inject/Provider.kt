package com.example.sophistnerd.inject

import com.example.sophistnerd.api.DownloadImageApi
import com.example.sophistnerd.api.UnsplashApi
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Component
class Provider {

    @Inject
    fun providerOkhttpClient() : OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Inject
    fun provideUnsplashApi(okHttpClient: OkHttpClient) : UnsplashApi {
        return Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }

    @Inject
    fun provideDownloadImageApi(okHttpClient: OkHttpClient) : DownloadImageApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .build()
            .create(DownloadImageApi::class.java)
    }
}