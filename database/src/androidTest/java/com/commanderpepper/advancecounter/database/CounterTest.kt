package com.commanderpepper.advancecounter.database

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import room.CounterDAO
import room.CounterDatabase
import java.io.IOException
import android.content.Context
import androidx.room.Room
import app.cash.turbine.test
import com.commanderpepper.advancecounter.database.model.Counter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CounterTest {
    private lateinit var counterDAO: CounterDAO
    private lateinit var database: CounterDatabase

    @Before
    fun createDatabase(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            CounterDatabase::class.java
        ).build()
        counterDAO = database.counterDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase(){
        database.close()
    }

    @Test
    fun insertCountersGetCounterList() = runTest {
        val counter = Counter(name = "Test", value = 1L)
        counterDAO.insertCounter(counter)
        counterDAO.insertCounter(counter)
        val counterFlow = counterDAO.getCounters()
        counterFlow.test {
            Assert.assertTrue(awaitItem().size == 2)
            cancelAndIgnoreRemainingEvents()
        }
    }
}