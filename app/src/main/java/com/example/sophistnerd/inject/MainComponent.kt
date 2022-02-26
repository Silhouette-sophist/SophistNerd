package com.example.sophistnerd.inject

import com.example.sophistnerd.component.MainActivity
import com.example.sophistnerd.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

@ApplicationScope
@Singleton
@Component(modules = [ProviderModule::class])
interface MainComponent {

    fun inject(mainViewModel: MainViewModel)

    fun inject(mainActivity: MainActivity)
}