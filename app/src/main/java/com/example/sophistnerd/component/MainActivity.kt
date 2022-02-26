package com.example.sophistnerd.component

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.sophistnerd.R
import com.example.sophistnerd.util.showSnackbarMessage
import com.example.sophistnerd.viewmodel.MainViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var imageView: ImageView
    private lateinit var searchKeywords: EditText

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initView()
        initLiveData()
    }

    private fun initView() {

        imageView = findViewById<ImageView>(R.id.image_view)
        searchKeywords = findViewById<EditText>(R.id.search_keywords)

        findViewById<Button>(R.id.search_image).setOnClickListener {
            //隐藏软键盘
            val manager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val text = searchKeywords.text
            if (text != null && text.trim().isNotEmpty()) {
                coroutineScope.launch {
                    viewModel.search(text.trim().toString())
                    showSnackbarMessage(it, "search finish")
                }
            }
        }

        findViewById<Button>(R.id.previous_image).setOnClickListener {
            viewModel.previous()
            showSnackbarMessage(it, "previous finish")
        }

        findViewById<Button>(R.id.next_image).setOnClickListener {
            viewModel.next()
            showSnackbarMessage(it, "next finish")
        }
    }

    private fun initLiveData() {
        viewModel.currentImage.observeForever {
            it.urls.regular?.let {
                coroutineScope.launch {
                    val imageBitmap = withContext(Dispatchers.IO) {
                        viewModel.downloadImage(it)
                    }
                    imageView.setImageBitmap(imageBitmap)
                    showSnackbarMessage(imageView, "previous finish")
                }
            }
        }
    }
}