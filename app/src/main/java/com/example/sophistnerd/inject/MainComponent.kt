package com.example.sophistnerd.inject

import com.example.sophistnerd.component.MainActivity
import com.example.sophistnerd.component.jetpack.MainLifecycleObserver
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import dagger.Component
import javax.inject.Singleton

@ApplicationScope
@Singleton
@Component(modules = [ProviderModule::class, LoggerModule::class])
interface MainComponent {

    fun inject(mainSavedStateViewModel: MainSavedStateViewModel)

    fun inject(mainActivity: MainActivity)

    fun inject(mainLifecycleObserver: MainLifecycleObserver)
}