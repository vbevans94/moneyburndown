package com.moneyburndown.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moneyburndown.extensions.plusDays
import com.moneyburndown.extensions.toEndOfDay
import com.moneyburndown.extensions.toStartOfDay
import com.moneyburndown.model.BurndownRepo
import com.moneyburndown.view.Confirm
import com.moneyburndown.view.Exit
import com.moneyburndown.view.SelectDate
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

class LimitViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private val mockRepo = mock<BurndownRepo>()
    private lateinit var viewModel: LimitViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        viewModel = LimitViewModel(mockRepo)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun selectDate_triggered() {
        viewModel.selectEndDate()

        assertTrue(viewModel.eventLiveData.value is SelectDate)
    }

    @Test
    fun onDateSelected_corrected() {
        val date = Date()
        viewModel.onDateSelected(date)

        val endOfDay = Calendar.getInstance().run {
            timeInMillis = date.time
            this[Calendar.HOUR_OF_DAY] = 23
            this[Calendar.MINUTE] = 59
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
            Date(timeInMillis)
        }
        assertEquals(endOfDay, viewModel.endDate.value)
    }

    @Test
    fun exit_emptyValue() {
        viewModel.exit(true)

        assertTrue(viewModel.error.value == true)
    }

    @Test
    fun exit_confirm() {
        viewModel.limitValue.value = "123"

        viewModel.exit(true)

        assertTrue(viewModel.eventLiveData.value == Confirm)
    }

    @Test
    fun exit_cancel() {
        viewModel.exit(false)

        assertTrue(viewModel.eventLiveData.value == Exit)
    }

    @Test
    fun confirmSave_no() {
        viewModel.confirmSave(false)

        assertTrue(viewModel.eventLiveData.value == Exit)
    }

    @Test
    fun confirmSave_yes() = runBlocking{
        whenever(mockRepo.resetLimit(anyInt(), any(), any())).thenReturn(Unit)

        val limit = "123"
        viewModel.limitValue.value = limit
        val start = Date()
        val end = start.plusDays(1)
        viewModel.onDateSelected(end)

        viewModel.confirmSave(true)

        assertTrue(viewModel.eventLiveData.value == Exit)
        verify(mockRepo).resetLimit(eq(limit.toInt()), eq(start.toStartOfDay()), eq(end.toEndOfDay()))
    }
}
