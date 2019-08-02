package com.bb.moneyburndown

import androidx.room.Room
import com.bb.moneyburndown.model.AppDatabase
import com.bb.moneyburndown.model.BurndownRepo
import com.bb.moneyburndown.viewmodel.BurndownViewModelFactory
import com.bb.moneyburndown.viewmodel.ChangeViewModelFactory
import com.bb.moneyburndown.viewmodel.LimitViewModelFactory
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