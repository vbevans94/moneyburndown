package com.bb.moneyburndown.model

import java.util.*

open class BurndownRepo(private val burndownDao: BurndownDao) {

    open suspend fun resetLimit(limit: Int, start: Date, end: Date) {
        burndownDao.resetLimit(Limit(value = limit, start = start.time, end = end.time))
    }

    open suspend fun addChange(change: Int, date: Date) {
        burndownDao.insertChange(Change(value = change, date = date.time))
    }

    open fun getChanges() = burndownDao.getChanges()

    open fun getLimit() = burndownDao.getLimit()

    companion object {
        val DEFAULT_START = Date()

        const val DEFAULT_DAYS = 30

        const val DEFAULT_LIMIT = 10_000
    }
}