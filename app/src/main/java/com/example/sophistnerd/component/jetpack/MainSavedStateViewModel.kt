package com.example.sophistnerd.component.jetpack

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophistnerd.SophistApplication
import com.example.sophistnerd.api.UnsplashApi
import com.example.sophistnerd.inject.DaggerMainComponent
import com.example.sophistnerd.service.DownloadImageImpl
import com.google.gson.Gson
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import java.util.logging.Logger
import javax.inject.Inject

class MainSavedStateViewModel : ViewModel() {

    companion object {
        private const val KEY_SP = "current_image"
    }

    private val sp : SharedPreferences by lazy {
        SophistApplication.sContext.getSharedPreferences("MainSavedStateViewModel", Context.MODE_PRIVATE)
    }
    val imageSource = MutableLiveData<ArrayList<UnsplashPhoto>>(ArrayList())
    val currentImage = MutableLiveData<UnsplashPhoto>()
    //后续对搜索框进行持久化
    private val searchKeywords = MutableLiveData<String>()
    private var index = 0

    //委托dagger进行依赖注入
    init {
        DaggerMainComponent.create().inject(this)

        val string = sp.getString(KEY_SP, "")
        Gson().fromJson(string, UnsplashPhoto::class.java)?.let {
            currentImage.value = it
        }
    }

    @Inject
    lateinit var unsplashApi: UnsplashApi
    @Inject
    lateinit var logger: Logger

    suspend fun search(keywords: String) = withContext(Dispatchers.IO) {
        searchKeywords.postValue(keywords)
        val unsplashResponse = unsplashApi.searchPhotos(keywords)
        index = 0

        imageSource.value?.clear()
        imageSource.value?.addAll(unsplashResponse.results)
        logger.info("loading keywords : $keywords with ${imageSource.value?.size}")
    }

    fun previous() {
        imageSource.value?.let {
            if (it.size > 0) {
                index--
                index = abs(index % it.size)
                currentImage.value = it[index]
                logger.info("previous image : $index / ${it.size}")
            }
        }
    }

    fun next() {
        imageSource.value?.let {
            if (it.size > 0) {
                index++
                index %= it.size
                currentImage.value = it[index]
                logger.info("next image : $index / ${it.size}")
            }
        }
    }

    suspend fun downloadImage(url: String): Bitmap {
        val bitmap = DownloadImageImpl().download(url)
        logger.info("downloadImage finish $index / ${imageSource.value?.size}")
        return bitmap
    }

    suspend fun refresh() {
        if (searchKeywords.value != null) {
            search(searchKeywords.value!!)
        }
    }

    fun save() {
        currentImage.value?.let {
            sp.edit().putString(KEY_SP, Gson().toJson(it)).apply()
        }
    }
}