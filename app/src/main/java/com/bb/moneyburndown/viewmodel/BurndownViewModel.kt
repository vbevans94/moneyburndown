package com.bb.moneyburndown.viewmodel

import androidx.lifecycle.*
import com.bb.moneyburndown.model.BurndownRepo
import com.bb.moneyburndown.model.Change
import com.bb.moneyburndown.model.Limit
import com.bb.moneyburndown.view.About
import com.bb.moneyburndown.view.AddChange
import com.bb.moneyburndown.view.SetLimit
import com.bb.moneyburndown.view.ViewEvent
import org.koin.core.KoinComponent
import org.koin.core.inject

class BurndownViewModel : ViewModel(), KoinComponent {

    private val repo: BurndownRepo by inject()

    private val _eventLiveData = MutableLiveData<ViewEvent>()
    val eventLiveData: LiveData<ViewEvent> = _eventLiveData

    private val _limit = MutableLiveData<Limit>().apply {
        value = Limit()
    }
    val limit = _limit

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