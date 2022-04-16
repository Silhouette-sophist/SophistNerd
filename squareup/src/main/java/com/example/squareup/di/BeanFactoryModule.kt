package com.example.squareup.di

import com.example.squareup.api.ExampleApi
import com.example.squareup.api.UnsplashApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import javax.inject.Singleton


@Module
class BeanFactoryModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideLogger() = Logger.getLogger("squareup")

    @Singleton
    @Provides
    fun provideOkhttp() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @RetrofitQualifier("ExampleApi")
    @Singleton
    @Provides
    fun provideExampleApiRetrofit(okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(ExampleApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @RetrofitQualifier("UnsplashApi")
    @Singleton
    @Provides
    fun provideUnsplashApiRetrofit(okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
   @Provides
   fun provideExampleApiProxy(@RetrofitQualifier("ExampleApi") retrofit: Retrofit) : ExampleApi {
       return retrofit.create(ExampleApi::class.java)
   }

   @Singleton
   @Provides
   fun provideUnsplashApiProxy(@RetrofitQualifier("UnsplashApi") retrofit: Retrofit) : UnsplashApi {
       return retrofit.create(UnsplashApi::class.java)
   }
}