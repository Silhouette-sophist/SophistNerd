package com.example.sophistnerd.component.jetpack

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.sophistnerd.inject.DaggerMainComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

class MainLifecycleObserver(val savedStateViewModel: MainSavedStateViewModel) : LifecycleObserver {

    init {
        DaggerMainComponent.create().inject(this)
    }

    @Inject
    lateinit var logger: Logger
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        logger.info("onCreate")
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        logger.info("onPause")
        coroutineScope.launch {
            savedStateViewModel.refresh()
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        logger.info("onResume")
    }
}