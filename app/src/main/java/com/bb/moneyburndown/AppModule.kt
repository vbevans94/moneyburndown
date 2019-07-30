package com.bb.moneyburndown

import androidx.room.Room
import com.bb.moneyburndown.model.AppDatabase
import com.bb.moneyburndown.model.BurndownRepo
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    single { Gson() }

    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app-database")
            .build()
            .burndownDao()
    }

    single { BurndownRepo(get()) }

    // factory { MySimplePresenter(get()) }
}