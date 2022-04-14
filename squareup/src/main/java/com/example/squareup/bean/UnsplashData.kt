package com.example.squareup

data class UnsplashData(val baseData : BaseData, val timeStamp : Long)

data class BaseData(val name : String, val grades : List<Grade>)

data class Grade(val gradeName : String, val goal : Int)
