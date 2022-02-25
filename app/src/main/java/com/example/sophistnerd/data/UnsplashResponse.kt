package com.example.sophistnerd.data

import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto

data class UnsplashResponse(val total: Int, val total_pages: Int, val results: List<UnsplashPhoto>)