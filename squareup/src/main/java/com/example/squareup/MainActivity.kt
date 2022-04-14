package com.example.squareup

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.squareup.api.UnsplashApi
import com.example.squareup.converter.CustomConverter
import com.example.squareup.util.SquareUpLog
import com.example.squareup.util.showToastMessage
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(UnsplashApi.BASE_URL)
            .build()
    }

    val unsplashApiProxy by lazy {
        retrofit.create(UnsplashApi::class.java)
    }

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler {_, throwable ->
        SquareUpLog.error(throwable)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       initView()
        //requestMyPermissions()
    }

    private fun initView() {
        findViewById<Button>(R.id.simple_param).setOnClickListener {
            coroutineScope.launch {
                val simpleResponse = withContext(Dispatchers.IO) {
                    unsplashApiProxy.postBaseInfo("simple_string")
                }
                showToastMessage(this@MainActivity, simpleResponse)
            }
        }

        findViewById<Button>(R.id.simple_response).setOnClickListener {
            showToastMessage(this@MainActivity, "simple_response not impl")
        }

        findViewById<Button>(R.id.complex_param).setOnClickListener {
            coroutineScope.launch {
                val unsplashApiProxy = retrofit.create(UnsplashApi::class.java)
                val grades = arrayListOf(Grade("math", 80), Grade("english", 79))
                val unsplashData = UnsplashData(BaseData("chenjin", grades), System.currentTimeMillis())

                val simpleResponse = withContext(Dispatchers.IO) {
                    unsplashApiProxy.postComplexInfo(unsplashData)
                }
                showToastMessage(this@MainActivity, simpleResponse)
            }
        }

        findViewById<Button>(R.id.complex_response).setOnClickListener {
            showToastMessage(this@MainActivity, "complex_response not impl")

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor{
                    SquareUpLog.info("interceptor --- ${it.request().url}")
                    it.proceed(it.request())
                }
                .addNetworkInterceptor {
                    SquareUpLog.info("network interceptor --- ${it.request().url}")
                    it.proceed(it.request())
                }
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            /**
             * 注意form-data传递方式--1
             */
            val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(Headers.headersOf("Content-Disposition", "form-data;name=unsplashData"),
                    "unsplashData-value".toRequestBody(null)
                )
                .build()

            /**
             * 注意form-data传递方式--2
             */
            val requestBodyMap = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("unsplashData", "form-data-part")
                .build()

            val request = Request.Builder()
                .url(UnsplashApi.BASE_URL + "/api/post/complex")
                .post(requestBodyMap)
                .build()

            val newCall = okHttpClient.newCall(request)

            newCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    SquareUpLog.info("onFailure --- $call ${e.cause} ${e.message}, ${e.stackTrace}")
                }

                override fun onResponse(call: Call, response: Response) {
                    SquareUpLog.info("onResponse --- $response")
                    //从请求响应体中
                    coroutineScope.launch {
                        showToastMessage(this@MainActivity, "读取开始")
                        withContext(Dispatchers.IO){
                            response.body?.byteStream()?.let {
                                val file = this@MainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                    ?.resolve("download-picture-${System.currentTimeMillis()}.png")
                                val readBytes = it.readBytes()
                                println("writeByteStream == ${readBytes.size}")
                                file?.writeBytes(readBytes)
                                println("writeByteStream finish == ${file?.absolutePath}")
                            }
                        }
                        showToastMessage(this@MainActivity, "读取完成")
                    }
                }
            })

            //val execute = newCall.execute()
        }
    }

    private fun requestMyPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }
}