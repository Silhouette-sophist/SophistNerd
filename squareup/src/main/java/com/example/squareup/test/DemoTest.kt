package com.example.squareup.test

import com.example.squareup.BaseData
import com.example.squareup.Grade
import com.example.squareup.UnsplashData
import com.example.squareup.api.UnsplashApi
import com.example.squareup.converter.CustomConverter
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit

fun main() {
    val retrofit = Retrofit.Builder()
        .baseUrl(UnsplashApi.BASE_URL)
        .addConverterFactory(CustomConverter())
        .build()

    val unsplashApiProxy = retrofit.create(UnsplashApi::class.java)

    val grades = arrayListOf(Grade("math", 80), Grade("english", 79))

    runBlocking {
        val postComplexInfo = unsplashApiProxy.postComplexInfo(
            UnsplashData(
                BaseData("chenjin", grades),
                System.currentTimeMillis()
            )
        )
        postComplexInfo.also {
            println("postComplexInfo == $it")
        }
    }
}