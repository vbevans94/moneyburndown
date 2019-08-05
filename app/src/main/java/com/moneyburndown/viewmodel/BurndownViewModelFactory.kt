package com.moneyburndown.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moneyburndown.model.BurndownRepo

@Suppress("UNCHECKED_CAST")
class BurndownViewModelFactory(
    private val repo: BurndownRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BurndownViewModel(repo) as T
    }
}