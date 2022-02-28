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
        private const val KEY_SP_CURRENT_IMAGE = "current_image"
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

        val string = sp.getString(KEY_SP_CURRENT_IMAGE, "")
        Gson().fromJson(string, UnsplashPhoto::class.java)?.let {
            currentImage.value = it
        }
    }

    @Inject
    lateinit var unsplashApi: UnsplashApi
    @Inject
    lateinit var logger: Logger

    suspend fun search(keywords: String, callback: ((String) -> Unit)? = null) = withContext(Dispatchers.IO) {
        searchKeywords.postValue(keywords)
        val unsplashResponse = unsplashApi.searchPhotos(keywords)
        index = 0

        imageSource.value?.clear()
        imageSource.value?.addAll(unsplashResponse.results)
        "loading keywords : $keywords with ${imageSource.value?.size}".also { msg ->
            callback?.invoke(msg)
            logger.info(msg)
        }
    }

    fun previous(callback: ((String) -> Unit)? = null) {
        imageSource.value?.let {
            if (it.size > 0) {
                index--
                index = abs(index % it.size)
                currentImage.value = it[index]
                "previous image : $index / ${it.size}".also { msg ->
                    callback?.invoke(msg)
                    logger.info(msg)
                }
            }
        }
    }

    fun next(callback: ((String) -> Unit)? = null) {
        imageSource.value?.let {
            if (it.size > 0) {
                index++
                index %= it.size
                currentImage.value = it[index]
                "next image : $index / ${it.size}".also { msg ->
                    callback?.invoke(msg)
                    logger.info(msg)
                }
            }
        }
    }

    suspend fun downloadImage(url: String, callback: ((String) -> Unit)? = null): Bitmap? {
        val bitmap = DownloadImageImpl().download(url)
        "downloadImage finish $index / ${imageSource.value?.size}".also {
            callback?.invoke(it)
            logger.info(it)
        }
        return bitmap
    }

    suspend fun refresh() {
        if (searchKeywords.value != null) {
            search(searchKeywords.value!!)
        }
    }

    fun saveCurrent() {
        currentImage.value?.let {
            sp.edit().putString(KEY_SP_CURRENT_IMAGE, Gson().toJson(it)).apply()
        }
    }

}