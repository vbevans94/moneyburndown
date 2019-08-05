package com.moneyburndown.model

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class BurndownRepoTest {

    private val testLimitLiveData = MutableLiveData<Limit>()
    private val testChangesLiveData = MutableLiveData<List<Change>>()

    private val mockDao = mock<BurndownDao>()
    private lateinit var burndownRepo: BurndownRepo

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        burndownRepo = BurndownRepo(mockDao)

        whenever(mockDao.getChanges()).thenReturn(testChangesLiveData)
        whenever(mockDao.getLimit()).thenReturn(testLimitLiveData)
    }

    @Test
    fun addChange() = runBlocking {
        val change = 100
        val date = Date()
        burndownRepo.addChange(change, date)
        verify(mockDao).insertChange(eq(Change(value = change, date = date.time)))
    }

    @Test
    fun resetLimit() = runBlocking {
        val limit = 100
        val start = Date()
        val end = Date()
        burndownRepo.resetLimit(limit, start, end)
        verify(mockDao).resetLimit(eq(Limit(value = limit, start = start.time, end = end.time)))
    }

    @Test
    fun getLimit() {
        assertEquals(testLimitLiveData, burndownRepo.getLimit())
    }

    @Test
    fun getChanges() {
        assertEquals(testChangesLiveData, burndownRepo.getChanges())
    }
}
