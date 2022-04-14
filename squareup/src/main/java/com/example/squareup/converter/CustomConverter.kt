package com.example.squareup.converter

import com.example.squareup.util.SquareUpLog
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * 请求和响应拦截时的转化处理
 */
class CustomConverter : Converter.Factory(){

    /**
     * 对responseBody进行转化处理
     */
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        SquareUpLog.info("responseBodyConverter...")
        return super.responseBodyConverter(type, annotations, retrofit)
    }

    /**
     * 对requestBody进行转化处理
     */
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        SquareUpLog.info("requestBodyConverter...")
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    /**
     * 对于String进行转化处理
     */
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        SquareUpLog.info("stringConverter...")
        return super.stringConverter(type, annotations, retrofit)
    }
}