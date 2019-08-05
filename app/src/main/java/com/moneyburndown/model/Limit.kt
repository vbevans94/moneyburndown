package com.moneyburndown.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moneyburndown.extensions.plusDays

@Entity(tableName = "limits")
data class Limit(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "start") var start: Long = BurndownRepo.DEFAULT_START.time,
    @ColumnInfo(name = "end") var end: Long = BurndownRepo.DEFAULT_START.plusDays(BurndownRepo.DEFAULT_DAYS).time,
    @ColumnInfo(name = "value") val value: Int = BurndownRepo.DEFAULT_LIMIT
)