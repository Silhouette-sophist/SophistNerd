package com.example.sophistnerd.component.jetpack

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophistnerd.api.UnsplashApi
import com.example.sophistnerd.inject.DaggerMainComponent
import com.example.sophistnerd.service.DownloadImageImpl
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import java.util.logging.Logger
import javax.inject.Inject

class MainViewModel : ViewModel() {

    //委托dagger进行依赖注入
    init {
        DaggerMainComponent.create().inject(this)
    }

    @Inject
    lateinit var unsplashApi: UnsplashApi
    @Inject
    lateinit var logger: Logger

    val imageSource = MutableLiveData<ArrayList<UnsplashPhoto>>(ArrayList())
    val currentImage = MutableLiveData<UnsplashPhoto>()

    private var index = 0
    private var lastKeywords: String? = null


    suspend fun search(keywords: String) = withContext(Dispatchers.IO) {
        lastKeywords = keywords
        val unsplashResponse = unsplashApi.searchPhotos(keywords)

        imageSource.value?.clear()
        imageSource.value?.addAll(unsplashResponse.results)
        logger.info("loading keywords : $keywords with ${imageSource.value?.size}")
    }

    fun previous() {
        imageSource.value?.let {
            index--
            index = abs(index % it.size)
            currentImage.value = it[index]
            logger.info("previous image : $index / ${it.size}")
        }
    }

    fun next() {
        imageSource.value?.let {
            index++
            index %= it.size
            currentImage.value = it[index]
            logger.info("next image : $index / ${it.size}")
        }
    }

    suspend fun downloadImage(url: String): Bitmap {
        val bitmap = DownloadImageImpl().download(url)
        logger.info("downloadImage finish $index / ${imageSource.value?.size}")
        return bitmap
    }

    suspend fun refresh() {
        if (lastKeywords != null) {
            search(lastKeywords!!)
        }
    }
}