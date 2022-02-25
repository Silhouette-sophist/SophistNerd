package com.example.sophistnerd.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophistnerd.api.DownloadImageApi
import com.example.sophistnerd.api.UnsplashApi
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject lateinit var unsplashApi : UnsplashApi
    @Inject lateinit var downloadImageApi: DownloadImageApi

    val currentImage = MutableLiveData<UnsplashPhoto>()

    val dataSource = mutableListOf<UnsplashPhoto>()
    private var index = 0


    suspend fun search(keywords: String) = withContext(Dispatchers.IO) {
        val unsplashResponse = unsplashApi.searchPhotos(keywords)
        dataSource.clear()
        dataSource.addAll(unsplashResponse.results)
    }

    fun previous() {
        if (dataSource.size != 0) {
            index--
            index = abs(index % dataSource.size)
            currentImage.value = dataSource[index]
        }
    }

    fun next() {
        if (dataSource.size != 0) {
            index++
            index %= dataSource.size
            currentImage.value = dataSource[index]
        }
    }

    suspend fun downloadImage(url : String) : Bitmap {
        /*val httpURLConnection = URL(url).openConnection() as HttpURLConnection
        httpURLConnection.doInput = true
        httpURLConnection.connectTimeout = 60000
        httpURLConnection.readTimeout = 60000
        httpURLConnection.connect()

        val inputStream = httpURLConnection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        return bitmap*/

        val responseBody = withContext(Dispatchers.IO) {
            downloadImageApi.download(url)
        }
        return BitmapFactory.decodeStream(responseBody.byteStream())
    }

}