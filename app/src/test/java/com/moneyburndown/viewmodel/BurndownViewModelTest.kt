package com.moneyburndown.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.moneyburndown.model.BurndownRepo
import com.moneyburndown.model.Change
import com.moneyburndown.model.Limit
import com.moneyburndown.view.About
import com.moneyburndown.view.AddChange
import com.moneyburndown.view.SetLimit
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class BurndownViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private val mockRepo = mock<BurndownRepo>()
    private lateinit var viewModel: BurndownViewModel
    private val testLimitLiveData = MutableLiveData<Limit>()
    private val testChangesLiveData = MutableLiveData<List<Change>>()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        whenever(mockRepo.getChanges()).thenReturn(testChangesLiveData)
        whenever(mockRepo.getLimit()).thenReturn(testLimitLiveData)
        viewModel = BurndownViewModel(mockRepo)
    }

    @Test
    fun addChange() {
        viewModel.addChange()

        assertTrue(viewModel.eventLiveData.value == AddChange)
    }

    @Test
    fun resetLimit() {
        viewModel.resetLimit()

        assertTrue(viewModel.eventLiveData.value == SetLimit)
    }

    @Test
    fun showAbout() {
        viewModel.showAbout()

        assertTrue(viewModel.eventLiveData.value == About)
    }

    @Test
    fun moneyLeft() {
        assertEquals(BurndownRepo.DEFAULT_LIMIT, viewModel.moneyLeft.value)

        val newLimit = Limit(value = 100)
        testLimitLiveData.value = newLimit

        assertEquals(newLimit.value, viewModel.moneyLeft.value)

        testChangesLiveData.value = listOf(Change(value = 10, date = Date().time))

        assertEquals(90, viewModel.moneyLeft.value)

        testChangesLiveData.value = listOf(Change(value = 110, date = Date().time))

        assertEquals(0, viewModel.moneyLeft.value)
    }

    @Test
    fun changes() {
        testChangesLiveData.value = listOf(Change(value = 10, date = Date().time))

        assertEquals(testChangesLiveData.value, viewModel.changes.value)
    }

    @Test
    fun limit() {
        testLimitLiveData.value = Limit(value = 100)

        assertEquals(testLimitLiveData.value, viewModel.limit.value)
    }
}
