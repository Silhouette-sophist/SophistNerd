package com.example.sophistnerd.inject

import dagger.Module
import dagger.Provides
import java.util.logging.Logger
import javax.inject.Singleton

@Module
class LoggerModule {

    @Singleton
    @Provides
    fun provideLogger(): Logger {
        return Logger.getLogger("SophistNerd")
    }
}