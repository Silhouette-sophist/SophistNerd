package com.example.extend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.extend.utils.showPositionLog

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        showPositionLog()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        showPositionLog()
    }

    override fun onStart() {
        super.onStart()
        showPositionLog()
    }

    override fun onResume() {
        super.onResume()
        showPositionLog()
    }

    override fun onPause() {
        super.onPause()
        showPositionLog()
    }

    override fun onStop() {
        super.onStop()
        showPositionLog()
    }

    override fun onDestroy() {
        super.onDestroy()
        showPositionLog()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        showPositionLog()
    }
}