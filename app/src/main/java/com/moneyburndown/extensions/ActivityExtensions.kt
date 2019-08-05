package com.moneyburndown.extensions

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

fun <T> FragmentActivity.fragmentsProvider(clazz: Class<T>, factory: ViewModelProvider.Factory) =
    ViewModelProviders.of(supportFragmentManager.findFragmentByTag(clazz.simpleName)
        ?: throw IllegalStateException("There should be fragment of $clazz"), factory)
