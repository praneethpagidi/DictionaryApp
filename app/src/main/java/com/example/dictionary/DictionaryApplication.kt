package com.example.dictionary

import android.app.Application
import com.example.dictionary.domain.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by PraNeeTh on 4/8/20
 */
class DictionaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@DictionaryApplication)
            modules(
                listOf(
                    viewModelModule,
                    repositoryImplModule,
                    apiModule,
                    serviceModule,
                    databaseModule
                )
            )
        }
    }
}