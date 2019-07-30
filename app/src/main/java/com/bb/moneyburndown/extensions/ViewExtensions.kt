package com.bb.moneyburndown.extensions

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes

fun View.getColor(@ColorRes colorRes: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        resources.getColor(colorRes, context.theme)
    else
        resources.getColor(colorRes)
}

fun View.openKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}