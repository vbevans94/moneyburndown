package com.moneyburndown.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.moneyburndown.model.BurndownRepo
import com.moneyburndown.model.Change
import com.moneyburndown.model.Limit
import com.moneyburndown.view.About
import com.moneyburndown.view.AddChange
import com.moneyburndown.view.SetLimit
import com.moneyburndown.view.ViewEvent

class BurndownViewModel(private val repo: BurndownRepo) : ViewModel() {

    private val _eventLiveData = MutableLiveData<ViewEvent>()
    val eventLiveData: LiveData<ViewEvent> = _eventLiveData

    private val _limit = MutableLiveData<Limit>().apply {
        value = Limit()
    }
    val limit: LiveData<Limit> = _limit

    private val limitObserver = Observer<Limit> {
        if (it == null) {
            _eventLiveData.value = SetLimit
        } else {
            _limit.value = it
        }

        updateTotal()
    }

    private val _changes = MutableLiveData<List<Change>>().apply {
        value = emptyList()
    }
    val changes: LiveData<List<Change>> = _changes

    private val changesObserver = Observer<List<Change>> {
        it?.apply { _changes.value = this }

        updateTotal()
    }

    private val _moneyLeft = MutableLiveData<Int>().apply {
        value = BurndownRepo.DEFAULT_LIMIT
    }
    val moneyLeft = _moneyLeft

    init {
        loadData()
    }

    private fun updateTotal() {
        var total = limit.value?.value ?: BurndownRepo.DEFAULT_LIMIT
        changes.value?.forEach {
            total -= it.value
        }
        if (total < 0) {
            total = 0
        }
        _moneyLeft.value = total
    }

    private fun loadData() {
        repo.getChanges().observeForever(changesObserver)
        repo.getLimit().observeForever(limitObserver)
    }

    fun addChange() {
        _eventLiveData.value = AddChange
    }

    fun resetLimit() {
        _eventLiveData.value = SetLimit
    }

    fun showAbout() {
        _eventLiveData.value = About
    }

    override fun onCleared() {
        repo.getChanges().removeObserver(changesObserver)
        repo.getLimit().removeObserver(limitObserver)
    }
}