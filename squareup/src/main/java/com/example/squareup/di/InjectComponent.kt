package com.example.squareup.di

import com.example.squareup.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [BeanFactoryModule::class])
interface InjectComponent {

    public fun inject(mainActivity: MainActivity)
}