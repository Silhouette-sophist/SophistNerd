package com.example.sophistnerd.component

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sophistnerd.R
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.example.sophistnerd.databinding.ActivityMainBinding
import com.example.sophistnerd.util.showMessageSafely
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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

        //改用DataBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigation.toolbar)

        binding.appBarNavigation.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.blank_fragment, R.id.item_fragment, R.id.picture_fragment), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //沉浸式，加上android:fitsSystemWindows="true"配置即可
        window.statusBarColor = Color.TRANSPARENT

        //和Activity关联的ViewModel，fragment可直接获取通过ActivityContext获取到。
        //主要ViewModel关联的是Activity的LifecycleOwner！
        saveStateViewModel = ViewModelProviders.of(this).get(MainSavedStateViewModel::class.java)

        //权限请求
        requestMyPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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