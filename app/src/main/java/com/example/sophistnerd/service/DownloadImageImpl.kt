package com.example.sophistnerd.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sophistnerd.api.DownloadImageApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageImpl : DownloadImageApi {

    override suspend fun download(url: String): Bitmap = withContext(Dispatchers.IO) {
        val httpURLConnection = URL(url).openConnection() as HttpURLConnection
        httpURLConnection.doInput = true
        httpURLConnection.connectTimeout = 60000
        httpURLConnection.readTimeout = 60000
        httpURLConnection.connect()

        val inputStream = httpURLConnection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        return@withContext bitmap
    }
}