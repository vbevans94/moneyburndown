package com.moneyburndown

import androidx.room.Room
import com.moneyburndown.model.AppDatabase
import com.moneyburndown.model.BurndownRepo
import com.moneyburndown.viewmodel.BurndownViewModelFactory
import com.moneyburndown.viewmodel.ChangeViewModelFactory
import com.moneyburndown.viewmodel.LimitViewModelFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app-database")
            .build()
            .burndownDao()
    }

    single { BurndownRepo(get()) }

    factory { LimitViewModelFactory(get()) }

    factory { ChangeViewModelFactory(get()) }

    factory { BurndownViewModelFactory(get()) }
}