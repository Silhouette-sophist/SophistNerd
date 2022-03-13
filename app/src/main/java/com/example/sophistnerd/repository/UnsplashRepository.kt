package com.example.sophistnerd.repository

import com.example.sophistnerd.api.UnsplashApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnsplashRepository(private val defaultDispatcher : CoroutineDispatcher = Dispatchers.Default) {

    @Inject lateinit var unsplashApi : UnsplashApi

    suspend fun searchKeywords(keysWords : String) = withContext(defaultDispatcher) {
        unsplashApi.searchPhotos(keysWords).results
    }

    suspend fun loadCacheUnsplashPhotos() = withContext(defaultDispatcher) {

    }
}