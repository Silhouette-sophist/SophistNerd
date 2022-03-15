package com.example.sophistnerd.inject

import com.example.sophistnerd.api.OperateTrackApi
import com.example.sophistnerd.api.UnsplashApi
import com.example.sophistnerd.api.converter.LenientGsonConverterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ProviderModule {

    @Singleton
    @Provides
    fun providerRetrofitLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

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

    /**
     * 调试使用
     * 会给OkHttpClient对象设位置httpLoggingInterceptor对象
     */
    @Singleton
    @Provides
    fun provideOperateTrackApi(httpLoggingInterceptor: HttpLoggingInterceptor) : OperateTrackApi {
        val okHttpClient = OkHttpClient
                    .Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
        return Retrofit.Builder()
            .baseUrl(OperateTrackApi.BASE_URL)
            .client(okHttpClient)
            //注意，要考虑到服务端不完全会返回json格式数据，还可能出现基本类型！
            .addConverterFactory(LenientGsonConverterFactory.create())
            .build()
            .create(OperateTrackApi::class.java)
    }
}