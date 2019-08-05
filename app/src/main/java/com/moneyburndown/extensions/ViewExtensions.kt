package com.moneyburndown.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat

fun View.getColor(@ColorRes colorRes: Int): Int {
    return ResourcesCompat.getColor(resources, colorRes, context.theme)
}