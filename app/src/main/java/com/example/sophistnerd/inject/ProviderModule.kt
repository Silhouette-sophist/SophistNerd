package com.example.sophistnerd.inject

import com.example.sophistnerd.api.OperateTrackApi
import com.example.sophistnerd.api.UnsplashApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ProviderModule {

    @Singleton
    @Provides
    fun providerOkhttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideUnsplashApi(okHttpClient: OkHttpClient): UnsplashApi {
        return Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOperateTrackApi(okHttpClient: OkHttpClient) : OperateTrackApi {
        return Retrofit.Builder()
            .baseUrl(OperateTrackApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OperateTrackApi::class.java)
    }
}