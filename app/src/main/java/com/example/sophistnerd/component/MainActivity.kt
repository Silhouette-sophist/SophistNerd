package com.example.sophistnerd.component

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.sophistnerd.R
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var saveStateViewModel: MainSavedStateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //沉浸式，加上android:fitsSystemWindows="true"配置即可
        window.statusBarColor = Color.TRANSPARENT

        //和Activity关联的ViewModel，fragment可直接获取通过ActivityContext获取到。
        //主要ViewModel关联的是Activity的LifecycleOwner！
        saveStateViewModel = ViewModelProviders.of(this).get(MainSavedStateViewModel::class.java)
    }
}