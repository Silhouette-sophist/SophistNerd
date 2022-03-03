package com.example.sophistnerd.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.MainThread
import com.example.sophistnerd.SophistApplication
import com.example.sophistnerd.data.UnsplashPhotoWithStatus
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.IllegalStateException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat


@MainThread
private fun showMessage(msg: String, showLong: Boolean = false) {
    Toast.makeText(
        SophistApplication.sContext,
        msg,
        if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

fun showMessageSafely(msg: String, showLong: Boolean = false) {
    if (!isMainThread()) {
        Handler(Looper.getMainLooper()).post {
            showMessage(msg, showLong)
        }
    } else {
        showMessage(msg, showLong)
    }
}

fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

@MainThread
fun showSnackbarMessage(anchorView: View, msg: String) {

    if (anchorView.isAttachedToWindow) {
        Snackbar.make(anchorView, msg, BaseTransientBottomBar.LENGTH_SHORT)
            .setAction("确定") {

            }
            .show()
    }
}

//增加偏函数支持！！！方便传递函数参数。1.偏函数实现，2.匿名函数实现，3.lambda表达式实现
fun <P1, P2, R> Function2<P1, P2, R>.partial1(p1: P1) = fun(p2: P2) = this(p1, p2)
fun <P1, P2, R> Function2<P1, P2, R>.partial2(p2: P2) = fun(p1: P1) = this(p1, p2)


//比较折中，后续直接缓存所有数据源，并记录当前index
fun ArrayList<UnsplashPhotoWithStatus>.getSpecific(unsplashPhoto: UnsplashPhoto) : UnsplashPhotoWithStatus? {
    this.forEach {
        if (it.unsplashPhoto.equals(unsplashPhoto)) {
            return it
        }
    }
    return null
}

//扩展支持各种类型的反序列化，包括列表类
internal inline fun <reified T> Gson.fromJsonExtend(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

fun getCurrentFormatTime(format : String) : String {
    return System.currentTimeMillis().run {
        SimpleDateFormat(format).format(this)
    }
}

fun getCurrentFormatTime() = getCurrentFormatTime("MM-dd-HH-mm-ss")

/**
 * 将视图保存为文件
 * [view] 视图对象，[fileName] 文件路径
 */
suspend fun saveBitmap(view: View, fileName: String) {
    withContext(Dispatchers.IO) {
        // 创建对应大小的bitmap
        val bitmap = Bitmap.createBitmap(
            view.width, view.height,
            Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        saveBitmap(bitmap, fileName)
    }
}

/**
 * 将视图保存为文件，默认以当前时间戳为文件名
 * [view] 视图对象
 */
suspend fun saveBitmap(view: View) {
    saveBitmap(view, getCurrentFormatTime())
}

suspend fun saveBitmap(bitmap: Bitmap, fileName: String = getCurrentFormatTime()) {
    //存储
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file = File(dir, "$fileName.png")
    var outStream: FileOutputStream? = null
    if (file.isDirectory) { //如果是目录不允许保存
        return
    }
    try {
        outStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            bitmap.recycle()
            outStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

suspend fun saveImageViewBitmap(view: ImageView) {
    saveImageViewBitmap(view, getCurrentFormatTime())
}

/**
 * 保存ImageView的源图
 */
suspend fun saveImageViewBitmap(view: ImageView, fileName: String?) {
    withContext(Dispatchers.IO) {
        val drawable: Drawable = view.drawable ?: return@withContext
        var outStream: FileOutputStream? = null

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(dir, "$fileName.png")

        try {
            outStream = FileOutputStream(file)
            val bitmap = (drawable as BitmapDrawable).bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * saveUrlImage下载网络图片
 * [urlPath] url地址
 */
suspend fun saveUrlImage(urlPath: String){
    withContext(Dispatchers.IO) {
        val httpURLConnection = URL(urlPath).openConnection() as HttpURLConnection
        httpURLConnection.doInput = true
        httpURLConnection.connectTimeout = 60000
        httpURLConnection.readTimeout = 60000
        httpURLConnection.connect()

        var bitmap : Bitmap? = null
        try {
            val inputStream = httpURLConnection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            saveBitmap(bitmap)
        } catch (e : Exception) {
            println("DownloadImageImpl $e")
        }
        return@withContext bitmap
    }
}

fun showPositionLog(msg : String = "") {

    val illegalStateException = IllegalStateException()
    val stackTraceElement = illegalStateException.stackTrace[1]
    val split = stackTraceElement.className.split(".")
    //println("SophistNerd $split")
    val className = split[split.size - 1]
    val methodName = stackTraceElement.methodName
    var positionMsg = "SophistNerd"
    println("$positionMsg $className $methodName $msg")
}

fun getMotionEventDesc(motionEvent: MotionEvent?) : String{
    return when(motionEvent?.action){
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
        MotionEvent.ACTION_UP -> "ACTION_UP"
        else -> {"${motionEvent?.action}"}
    }
}