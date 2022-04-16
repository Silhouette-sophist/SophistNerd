package com.example.squareup

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.squareup.api.ExampleApi
import com.example.squareup.api.UnsplashApi
import com.example.squareup.databinding.ActivityMainBinding
import com.example.squareup.di.DaggerInjectComponent
import com.example.squareup.di.RetrofitQualifier
import com.example.squareup.util.showToastMessage
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    //todo 同意类型对象，需要用Quealifier去区分，不然报错。注意是kotlin字段的field上加注解
    @field:RetrofitQualifier("UnsplashApi")
    @Inject
    lateinit var retrofit2: Retrofit

    //todo 同意类型对象，需要用Quealifier去区分，不然报错。注意是kotlin字段的field上加注解
    @field:RetrofitQualifier("ExampleApi")
    @Inject
    lateinit var retrofit : Retrofit

    @Inject
    lateinit var unsplashApiProxy: UnsplashApi

    @Inject
    lateinit var exampleApiProxy: ExampleApi

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var okHttpClient: OkHttpClient

    lateinit var root : ActivityMainBinding

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler {_, throwable ->
        val errorMsg = "[${throwable.message}, ${throwable.cause}, ${Arrays.toString(throwable.stackTrace)}]"
        logger.warning(errorMsg)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        root = ActivityMainBinding.inflate(layoutInflater)

       initView()
        //requestMyPermissions()

        //todo 注意，需要在各种被注入对象使用前完成注入，因此，将注入放到onCreate方法中
        DaggerInjectComponent.create().inject(this)

        //todo 上面已经对MainActivity对象完成了依赖注入，所以下面可以直接使用了！！！
        logger.info("$retrofit, $unsplashApiProxy $okHttpClient")
    }

    private fun initView() {
        root.simpleParam.setOnClickListener {
            coroutineScope.launch {
                val simpleResponse = withContext(Dispatchers.IO) {
                    unsplashApiProxy.postBaseInfo("simple_string")
                }
                showToastMessage(this@MainActivity, simpleResponse)
            }
        }

        root.simpleResponse.setOnClickListener {
            showToastMessage(this@MainActivity, "simple_response not impl")
        }

        root.complexParam.setOnClickListener {
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

        root.complexResponse.setOnClickListener {
            showToastMessage(this@MainActivity, "complex_response not impl")

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor{
                    logger.info("interceptor --- ${it.request().url}")
                    it.proceed(it.request())
                }
                .addNetworkInterceptor {
                    logger.info("network interceptor --- ${it.request().url}")
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
                    logger.warning("onFailure --- $call ${e.cause} ${e.message}, ${e.stackTrace}")
                }

                override fun onResponse(call: Call, response: Response) {
                    logger.info("onResponse --- $response")
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