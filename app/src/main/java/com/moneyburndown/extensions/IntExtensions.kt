package com.moneyburndown.extensions

fun Int.toSmallString(): String {
    return if (this >= 1000) "${this / 1000}K" else this.toString()
}