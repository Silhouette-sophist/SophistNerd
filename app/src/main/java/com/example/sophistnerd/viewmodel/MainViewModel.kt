package com.example.sophistnerd.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophistnerd.api.UnsplashApi
import com.example.sophistnerd.inject.DaggerMainComponent
import com.example.sophistnerd.service.DownloadImageImpl
import com.example.sophistnerd.util.showMessageSafely
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import javax.inject.Inject

class MainViewModel : ViewModel() {

    //委托dagger进行依赖注入
    init {
        DaggerMainComponent.create().inject(this)
    }

    @Inject
    lateinit var unsplashApi: UnsplashApi

    val currentImage = MutableLiveData<UnsplashPhoto>()
    private val dataSource = mutableListOf<UnsplashPhoto>()
    private var index = 0


    suspend fun search(keywords: String) = withContext(Dispatchers.IO) {
        val unsplashResponse = unsplashApi.searchPhotos(keywords)
        dataSource.clear()
        dataSource.addAll(unsplashResponse.results)
        showMessageSafely("loading keywords : $keywords with ${dataSource.size}")
    }

    fun previous() {
        if (dataSource.size != 0) {
            index--
            index = abs(index % dataSource.size)
            currentImage.value = dataSource[index]
            showMessageSafely("previous image : $index / ${dataSource.size}")
        }
    }

    fun next() {
        if (dataSource.size != 0) {
            index++
            index %= dataSource.size
            currentImage.value = dataSource[index]
            showMessageSafely("next image : $index / ${dataSource.size}")
        }
    }

    suspend fun downloadImage(url: String): Bitmap {
        val bitmap = DownloadImageImpl().download(url)
        showMessageSafely("downloadImage finish $index / ${dataSource.size}")
        return bitmap
    }

}