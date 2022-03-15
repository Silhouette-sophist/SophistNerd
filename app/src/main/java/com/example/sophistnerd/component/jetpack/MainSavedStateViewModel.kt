package com.example.sophistnerd.component.jetpack

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophistnerd.SophistApplication
import com.example.sophistnerd.api.OperateTrackApi
import com.example.sophistnerd.api.UnsplashApi
import com.example.sophistnerd.data.UnsplashPhotoWithStatus
import com.example.sophistnerd.inject.DaggerMainComponent
import com.example.sophistnerd.service.DownloadImageImpl
import com.example.sophistnerd.util.fromJsonExtend
import com.google.gson.Gson
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.*
import java.io.File
import java.lang.Math.abs
import java.util.logging.Logger
import javax.inject.Inject

class MainSavedStateViewModel : ViewModel() {

    companion object {
        private const val KEY_SP_CURRENT_IMAGE_INDEX = "current_image_index"
        private const val KEY_SP_IMAGE_SOURCE = "image_source"
    }

    private val sp : SharedPreferences by lazy {
        SophistApplication.sContext.getSharedPreferences("MainSavedStateViewModel", Context.MODE_PRIVATE)
    }
    //可变类型不直接对外公开
    private val _imageSource = MutableLiveData<ArrayList<UnsplashPhotoWithStatus>>(ArrayList())
    val imageSource = _imageSource
    //注意：当前图片的加载逻辑，首先上一张和下一张触发当前图片的修改，随后当前图片LiveData注册的观察者被通知到，然后会获取imageSource中的图片
    //实际上缓存index比较好，但是为了能够恢复当前图片就设置为UnsplashPhoto对象。注意，下载完成后会把bitmap记录到imageSource中
    private val _indexLiveData = MutableLiveData<Int>()
    val indexValue = _indexLiveData
    //用于辅助计算indexLiveData的字段
    private var index = 0
    //后续对搜索框进行持久化
    private val searchKeywords = MutableLiveData<String>()
    //注意SupervisorJob+CoroutineExceptionHandler一起使用，才不会导致子协程崩溃影响到父协程！！！
    private val ioCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, exception ->
        logger.info("MainSavedStateViewModel got $exception")
    })

    //委托dagger进行依赖注入
    init {
        DaggerMainComponent.create().inject(this)

        _indexLiveData.value = sp.getInt(KEY_SP_CURRENT_IMAGE_INDEX, 0)
        index = _indexLiveData.value!!

        val dataSource = sp.getString(KEY_SP_IMAGE_SOURCE, "")?.let{
            Gson().fromJsonExtend<ArrayList<UnsplashPhotoWithStatus>>(it)
        }
        if (dataSource != null && dataSource.isNotEmpty()) {
            _imageSource.value = dataSource!!
        }
    }

    @Inject
    lateinit var unsplashApi: UnsplashApi
    @Inject
    lateinit var logger: Logger
    @Inject
    lateinit var operateTrackApi: OperateTrackApi

    suspend fun search(keywords: String, callback: ((String) -> Unit)? = null) = withContext(Dispatchers.IO) {
        searchKeywords.postValue(keywords)
        val unsplashResponse = unsplashApi.searchPhotos(keywords)
        index = 0

        _imageSource.value?.clear()
        unsplashResponse.results.forEach {
            _imageSource.value?.add(UnsplashPhotoWithStatus(it))
        }
        withContext(Dispatchers.IO) {
            SophistApplication.sContext.getExternalFilesDir(null)
                ?.resolve("unsplash_resource")
                ?.apply { mkdirs() }.also {
                    File(it, "unsplash.json")
                        .writeText(Gson().toJson(unsplashResponse.results))
                }
        }
        "loading keywords : $keywords with ${_imageSource.value?.size}".also { msg ->
            callback?.invoke(msg)
            logger.info(msg)
            //注意不要用async，要用launch。async需要在去获取结果时才会抛出一场，而launch是在执行时！！！
            ioCoroutineScope.launch {
                operateTrackApi.uploadSearchKeywords(keywords, unsplashResponse.results)
            }
        }
        //注意这里是io线程，需要用postValue
        _indexLiveData.postValue(0)
    }

    fun previous(callback: ((String) -> Unit)? = null) {
        _imageSource.value?.let {
            if (it.size > 0) {
                index--
                index = abs(index % it.size)
                _indexLiveData.value = index
                "previous image : ${_indexLiveData.value} / ${it.size}".also { msg ->
                    callback?.invoke(msg)
                    logger.info(msg)
                }
            } else {
                callback?.invoke("no previous image")
            }
        }
    }

    fun next(callback: ((String) -> Unit)? = null) {
        _imageSource.value?.let {
            if (it.size > 0) {
                index++
                index %= it.size
                _indexLiveData.value = index
                "next image : ${_indexLiveData.value} / ${it.size}".also { msg ->
                    callback?.invoke(msg)
                    logger.info(msg)
                }
            } else {
                callback?.invoke("no next image")
            }
        }
    }

    suspend fun downloadImage(url: String, callback: ((String) -> Unit)? = null): Bitmap? {
        val bitmap = DownloadImageImpl().download(url)
        "downloadImage finish ${_indexLiveData.value} / ${_imageSource.value?.size}".also {
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
        _indexLiveData.value?.let {
            sp.edit().putInt(KEY_SP_CURRENT_IMAGE_INDEX, it).apply()
        }
        _imageSource.value?.let {
            val json = Gson().toJson(it)
            sp.edit().putString(KEY_SP_IMAGE_SOURCE, json).apply()
        }
    }

    suspend fun saveUrlImage(urlPath: String, type : String, unsplashPhoto: UnsplashPhoto){
        com.example.sophistnerd.util.saveUrlImage(urlPath)
        //注意不要用async，要用launch。async需要在去获取结果时才会抛出一场，而launch是在执行时！！！
        ioCoroutineScope.launch{
            operateTrackApi.uploadDownloadImage(type, unsplashPhoto)
        }
    }

}