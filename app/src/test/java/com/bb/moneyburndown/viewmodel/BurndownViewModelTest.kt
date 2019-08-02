package com.bb.moneyburndown.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bb.moneyburndown.model.BurndownRepo
import com.bb.moneyburndown.model.Change
import com.bb.moneyburndown.model.Limit
import com.bb.moneyburndown.view.About
import com.bb.moneyburndown.view.AddChange
import com.bb.moneyburndown.view.Exit
import com.bb.moneyburndown.view.SetLimit
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import java.util.*

class BurndownViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

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
        Dispatchers.setMain(Dispatchers.Unconfined)
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
