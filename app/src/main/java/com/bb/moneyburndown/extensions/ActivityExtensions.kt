package com.bb.moneyburndown.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders

fun <T> FragmentActivity.fragmentsProvider(clazz: Class<T>) =
    ViewModelProviders.of(supportFragmentManager.findFragmentByTag(clazz.simpleName)
        ?: throw IllegalStateException("There should be fragment of $clazz"))
