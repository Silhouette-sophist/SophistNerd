package com.example.extend

import android.os.Bundle
import android.view.MotionEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.extend.databinding.ActivityMainBinding
import com.example.extend.utils.getMotionEventDesc
import com.example.extend.utils.showPositionLog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        showPositionLog(getMotionEventDesc(event))
        return super.onTouchEvent(event)
    }
}