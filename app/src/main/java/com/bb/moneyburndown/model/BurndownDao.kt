package com.bb.moneyburndown.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BurndownDao {

    @Query("SELECT * FROM changes")
    fun getChanges(): LiveData<List<Change>>

    @Query("SELECT * FROM limits LIMIT 1")
    fun getLimit(): LiveData<Limit>

    @Insert
    suspend fun insertChange(change: Change)

    @Query("DELETE FROM limits")
    suspend fun deleteLimits()

    @Query("DELETE FROM changes")
    suspend fun deleteChanges()

    @Insert
    suspend fun insertLimit(limit: Limit)

    @Transaction
    suspend fun resetLimit(limit: Limit) {
        deleteLimits()
        deleteChanges()
        insertLimit(limit)
    }
}