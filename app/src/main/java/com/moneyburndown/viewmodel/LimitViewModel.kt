package com.moneyburndown.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneyburndown.extensions.plusDays
import com.moneyburndown.extensions.toEndOfDay
import com.moneyburndown.model.BurndownRepo
import com.moneyburndown.view.Confirm
import com.moneyburndown.view.Exit
import com.moneyburndown.view.SelectDate
import com.moneyburndown.view.ViewEvent
import kotlinx.coroutines.launch
import java.util.*

class LimitViewModel(private val repo: BurndownRepo) : ViewModel() {

    var endDate = MutableLiveData<Date>().apply {
        value = BurndownRepo.startDate().plusDays(BurndownRepo.DEFAULT_DAYS)
    }

    var limitValue = MutableLiveData<String>().apply {
        value = ""
    }

    private val _eventLiveData = MutableLiveData<ViewEvent>()
    val eventLiveData: LiveData<ViewEvent> = _eventLiveData

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun selectEndDate() {
        val date = endDate
        val minDate = BurndownRepo.startDate()
        val maxDate = null
        _eventLiveData.value = SelectDate(date.value!!, minDate, maxDate)
    }

    fun onDateSelected(date: Date) {
        endDate.value = date.toEndOfDay()
    }

    fun exit(save: Boolean) {
        if (save) {
            if (limitValue.value.isNullOrEmpty()) {
                _error.value = true
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
                repo.resetLimit(limitValue.value?.toInt() ?: 0, BurndownRepo.startDate(), endDate.value ?: Date())

                _eventLiveData.value = Exit
            }
        } else {
            _eventLiveData.value = Exit
        }
    }
}