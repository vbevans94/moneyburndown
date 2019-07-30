package com.bb.moneyburndown.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.moneyburndown.R
import com.bb.moneyburndown.model.BurndownRepo
import com.bb.moneyburndown.view.Exit
import com.bb.moneyburndown.view.ViewEvent
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class ChangeViewModel : ViewModel(), KoinComponent {

    private val repo: BurndownRepo by inject()
    private val context: Application by inject()

    var change = MutableLiveData<String>().apply { value = "" }

    private val _eventLiveData = MutableLiveData<ViewEvent>()
    val eventLiveData: LiveData<ViewEvent> = _eventLiveData

    private var option: Int = OPTION_EXPENSE

    private val _error = MutableLiveData<CharSequence>()
    val error: LiveData<CharSequence> = _error

    fun exit(save: Boolean) {
        if (save) {
            if (change.value.isNullOrEmpty()) {
                _error.value = context.getString(R.string.error_required)
            } else {
                viewModelScope.launch {
                    var delta = change.value?.toInt() ?: 0
                    if (option == OPTION_INCOME) {
                        delta = -delta
                    }
                    repo.addChange(delta, Date())

                    _eventLiveData.value = Exit
                }
            }
        } else {
            _eventLiveData.value = Exit
        }
    }

    fun selectOption(value: Int) {
        option = value
    }

    companion object {
        const val OPTION_EXPENSE = 0
        const val OPTION_INCOME = 1
    }
}