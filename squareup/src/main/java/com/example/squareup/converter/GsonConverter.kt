package com.example.squareup.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

/**
 * 模仿GsonRequestBodyConverter
 * - 将指定T类型的参数转化为RequestBody参数
 */
class GsonConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) : Converter<T, RequestBody>{

    private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    private val UTF_8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}