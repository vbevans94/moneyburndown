package com.moneyburndown.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "changes")
data class Change(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "value") val value: Int,
    @ColumnInfo(name = "date") var date: Long
)