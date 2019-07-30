package com.bb.moneyburndown.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.moneyburndown.R
import com.bb.moneyburndown.extensions.plusDays
import com.bb.moneyburndown.extensions.toEndOfDay
import com.bb.moneyburndown.model.BurndownRepo
import com.bb.moneyburndown.view.*
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class LimitViewModel : ViewModel(), KoinComponent {

    private val repo: BurndownRepo by inject()
    private val context: Application by inject()

    var startDate = MutableLiveData<Date>().apply {
        value = BurndownRepo.DEFAULT_START
    }

    var endDate = MutableLiveData<Date>().apply {
        value = BurndownRepo.DEFAULT_START.plusDays(BurndownRepo.DEFAULT_DAYS)
    }

    var limitValue = MutableLiveData<String>().apply {
        value = ""
    }

    private val _eventLiveData = MutableLiveData<ViewEvent>()
    val eventLiveData: LiveData<ViewEvent> = _eventLiveData

    private val _error = MutableLiveData<CharSequence>()
    val error: LiveData<CharSequence> = _error

    private var dateType = START_DATE

    fun selectDate(type: Int) {
        dateType = type
        val date = if (type == START_DATE) startDate else endDate
        val minDate = if (type == START_DATE) Date() else startDate.value!!
        val maxDate = if (type == START_DATE) endDate.value else null
        _eventLiveData.value = SelectDate(date.value!!, minDate, maxDate)
    }

    fun onDateSelected(date: Date) {
        if (dateType == START_DATE) {
            startDate.value = date
        } else {
            endDate.value = date.toEndOfDay()
        }
    }

    fun exit(save: Boolean) {
        if (save) {
            if (limitValue.value.isNullOrEmpty()) {
                _error.value = context.getString(R.string.error_required)
            } else {
                _eventLiveData.value = Confirm
            }
        } else {
            _eventLiveData.value = Exit
        }
    }

    fun confirmSave(confirm: Boolean) {
        if (confirm) {
            viewModelScope.launch {
                repo.resetLimit(limitValue.value?.toInt() ?: 0, startDate.value ?: Date(), endDate.value ?: Date())

                _eventLiveData.value = Exit
            }
        } else {
            _eventLiveData.value = Exit
        }
    }

    companion object {
        const val START_DATE = 0
        const val END_DATE = 1
    }
}