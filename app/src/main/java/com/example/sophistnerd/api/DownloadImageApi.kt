package com.example.sophistnerd.api

import okhttp3.ResponseBody

interface DownloadImageApi {

    suspend fun download(url : String) : ResponseBody
}