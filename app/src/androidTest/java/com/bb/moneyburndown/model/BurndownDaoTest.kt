package com.bb.moneyburndown.model

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.bb.moneyburndown.IsMainExecutorRule
import com.bb.moneyburndown.getValueBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class BurndownDaoTest {

    @get:Rule
    val liveDataRule = IsMainExecutorRule()

    private lateinit var burndownDao: BurndownDao
    private lateinit var database: AppDatabase

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        val context = getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        burndownDao = database.burndownDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetChanges() = runBlocking {
        val change = Change(id = 1, value = 100, date = 100L)
        burndownDao.insertChange(change)
        assertThat(burndownDao.getChanges().getValueBlocking()[0], equalTo(change))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetLimit() = runBlocking {
        val limit = Limit(id = 1, value = 100, start = 100L, end = 200L)
        burndownDao.insertLimit(limit)
        assertThat(burndownDao.getLimit().getValueBlocking(), equalTo(limit))
    }

    @Test
    @Throws(Exception::class)
    fun resetLimit() = runBlocking {
        burndownDao.insertChange(Change(value = 100, date = 100L))
        burndownDao.insertChange(Change(value = 10, date = 130L))
        val limit = Limit(id = 1, value = 100, start = 100L, end = 200L)
        burndownDao.resetLimit(limit)
        assertTrue(burndownDao.getChanges().getValueBlocking().isEmpty())
    }
}