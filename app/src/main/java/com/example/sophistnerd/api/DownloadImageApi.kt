package com.example.sophistnerd.api

import android.graphics.Bitmap

interface DownloadImageApi {

    suspend fun download(url: String): Bitmap?
}