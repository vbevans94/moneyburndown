package com.moneyburndown.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Limit::class, Change::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun burndownDao(): BurndownDao
}