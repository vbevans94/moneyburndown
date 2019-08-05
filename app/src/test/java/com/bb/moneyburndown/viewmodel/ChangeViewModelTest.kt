package com.bb.moneyburndown.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bb.moneyburndown.model.BurndownRepo
import com.bb.moneyburndown.view.Exit
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

class ChangeViewModelTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    private val mockRepo = mock<BurndownRepo>()
    private lateinit var viewModel: ChangeViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        viewModel = ChangeViewModel(mockRepo)
    }

    @Test
    fun exit_cancel() {
        viewModel.exit(false)

        assertTrue(viewModel.eventLiveData.value == Exit)
    }

    @Test
    fun exit_save_error() {
        viewModel.exit(true)

        assertTrue(viewModel.error.value == true)
    }

    @Test
    fun exit_save() = runBlocking {
        whenever(mockRepo.addChange(anyInt(), any())).thenReturn(Unit)
        val change = "123"
        viewModel.change.value = change

        viewModel.exit(true)

        verify(mockRepo).addChange(eq(change.toInt()), any())
        assertTrue(viewModel.eventLiveData.value == Exit)
    }

    @Test
    fun exit_save_income() = runBlocking {
        whenever(mockRepo.addChange(anyInt(), any())).thenReturn(Unit)
        val change = "123"
        viewModel.selectOption(ChangeViewModel.OPTION_INCOME)
        viewModel.change.value = change

        viewModel.exit(true)

        verify(mockRepo).addChange(eq(-change.toInt()), any())
        assertTrue(viewModel.eventLiveData.value == Exit)
    }
}
