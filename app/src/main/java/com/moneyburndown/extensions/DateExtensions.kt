package com.moneyburndown.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.toEndOfDay() = Calendar.getInstance().run {
    this.timeInMillis = this@toEndOfDay.time
    this[Calendar.HOUR_OF_DAY] = 23
    this[Calendar.MINUTE] = 59
    this[Calendar.SECOND] = 0
    Date(timeInMillis)
}

fun Date.toStartOfDay() = Calendar.getInstance().run {
    this.timeInMillis = this@toStartOfDay.time
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MINUTE] = 0
    this[Calendar.SECOND] = 0
    Date(timeInMillis)
}

fun Date.plusDays(days: Int): Date {
    return Date(time + TimeUnit.DAYS.toMillis(days.toLong()))
}

fun Date.toCalendar(): Calendar = Calendar.getInstance().apply {
    timeInMillis = this@toCalendar.time
}

fun fromComponents(year: Int, month: Int, dayOfMonth: Int) = Calendar.getInstance().run {
    this[Calendar.YEAR] = year
    this[Calendar.MONTH] = month
    this[Calendar.DAY_OF_MONTH] = dayOfMonth
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MINUTE] = 0
    Date(timeInMillis)
}

private val DATE_FORMAT = SimpleDateFormat("MMM d", Locale.ENGLISH)
fun Date.dayOfMonthString(): String {
    return DATE_FORMAT.format(this)
}

private val FULL_DATE_FORMAT = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
fun Date.fullDate(): String {
    return FULL_DATE_FORMAT.format(this)
}