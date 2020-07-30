package com.example.dictionary.domain.di

import android.app.Application
import androidx.room.Room
import com.example.dictionary.BuildConfig
import com.example.dictionary.domain.database.DefinitionDao
import com.example.dictionary.domain.database.DefinitionDatabase
import com.example.dictionary.domain.database.repository.DefinitionDatabaseRepository
import com.example.dictionary.domain.database.repository.DefinitionDatabaseRepositoryImpl
import com.example.dictionary.domain.service.DefinitionService
import com.example.dictionary.domain.service.repository.DefinitionServiceRepository
import com.example.dictionary.domain.service.repository.DefinitionServiceRepositoryImpl
import com.example.dictionary.ui.viewmodels.SearchViewModel
import com.example.dictionary.util.BASE_URL
import com.example.dictionary.util.DB_NAME
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by PraNeeTh on 4/8/20
 */

val viewModelModule = module {
    factory { SearchViewModel(get(), get()) }
}

val repositoryImplModule = module {
    factory<DefinitionServiceRepository> { DefinitionServiceRepositoryImpl(get()) }
    factory<DefinitionDatabaseRepository> { DefinitionDatabaseRepositoryImpl(get()) }
}

val apiModule = module {
    fun provideDefinitionService(retrofit: Retrofit): DefinitionService {
        return retrofit.create(DefinitionService::class.java)
    }
    single { provideDefinitionService(get()) }
}

val serviceModule = module {
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.callTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}

val databaseModule = module {
    fun provideDataBase(application: Application): DefinitionDatabase = with(
        Room.databaseBuilder(application, DefinitionDatabase::class.java, DB_NAME)
    )
    {
        fallbackToDestructiveMigration().build()
    }

    fun provideDao(database: DefinitionDatabase): DefinitionDao = database.definitionDao

    single { provideDataBase(androidApplication()) }
    factory { provideDao(get()) }
}