package com.bb.moneyburndown.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

val Fragment.myActivity: FragmentActivity
    get() = activity ?: throw IllegalStateException("Activity shouldn't be null")