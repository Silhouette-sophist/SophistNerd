package com.example.sophistnerd.component

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.sophistnerd.R
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.example.sophistnerd.util.showMessageSafely

class MainActivity : AppCompatActivity() {

    companion object {
        const val THRESHOLD = 200
    }
    //手指按下的点为(pointDownX, pointDownY)手指离开屏幕的点为(pointUpX, pointUpY)
    private var pointDownX = 0f
    private var pointDownY = 0f
    private var pointUpX = 0f
    private var pointUpY = 0f

    private lateinit var saveStateViewModel: MainSavedStateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //沉浸式，加上android:fitsSystemWindows="true"配置即可
        window.statusBarColor = Color.TRANSPARENT

        //和Activity关联的ViewModel，fragment可直接获取通过ActivityContext获取到。
        //主要ViewModel关联的是Activity的LifecycleOwner！
        saveStateViewModel = ViewModelProviders.of(this).get(MainSavedStateViewModel::class.java)

        requestMyPermissions()
    }

    override fun onPause() {
        super.onPause()

        saveStateViewModel.saveCurrent()
    }

    /**
     * 注意当事件没消费时，最后交由Activity处理
     * 注意事件传递：Activity--ViewGroup--View****View--ViewGroup--Activity
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.action == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            pointDownX = event.x
            pointDownY = event.y
        }
        if (event.action == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            pointUpX = event.x
            pointUpY = event.y
            when {
                pointDownY - pointUpY > THRESHOLD -> {
                    showMessageSafely("SwipeUp")
                }
                pointUpY - pointDownY > THRESHOLD -> {
                    showMessageSafely("SwipeDown")
                }
                pointDownX - pointUpX > THRESHOLD -> {
                    saveStateViewModel.next(::showMessageSafely)
                }
                pointUpX - pointDownX > THRESHOLD -> {
                    saveStateViewModel.previous(::showMessageSafely)
                }
            }
        }
        return super.onTouchEvent(event)
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