package com.example.squareup.component

import com.example.squareup.api.UnsplashApi
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addCallAdapterFactory(object : CallAdapter.Factory() {
                override fun get(
                    returnType: Type,
                    annotations: Array<out Annotation>,
                    retrofit: Retrofit
                ): CallAdapter<*, *>? {
                    TODO("Not yet implemented")
                }
            })
            .addConverterFactory(object : Converter.Factory() {
                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<out Annotation>,
                    retrofit: Retrofit
                ): Converter<ResponseBody, *>? {
                    return super.responseBodyConverter(type, annotations, retrofit)
                }

                override fun requestBodyConverter(
                    type: Type,
                    parameterAnnotations: Array<out Annotation>,
                    methodAnnotations: Array<out Annotation>,
                    retrofit: Retrofit
                ): Converter<*, RequestBody>? {
                    return super.requestBodyConverter(
                        type,
                        parameterAnnotations,
                        methodAnnotations,
                        retrofit
                    )
                }

                override fun stringConverter(
                    type: Type,
                    annotations: Array<out Annotation>,
                    retrofit: Retrofit
                ): Converter<*, String>? {
                    return super.stringConverter(type, annotations, retrofit)
                }
            })
            .build()
    }

    fun getOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            /*.interceptors()
            .addInterceptor {
                it.proceed(it.request())
            }
            .addNetworkInterceptor {
                it.proceed(it.request())
            }*/
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}