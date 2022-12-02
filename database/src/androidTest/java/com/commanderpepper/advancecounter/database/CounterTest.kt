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
    fun getCounterListCheckIsEmpty() = runTest {
        counterDAO.getCounters().test {
            Assert.assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNonExistentCounterCheckCounterIsNull() = runTest {
        val counterThatShouldBeNull = counterDAO.getCounter(0L)
        Assert.assertNull(counterThatShouldBeNull)
    }

    @Test
    fun insertCounterUpdateCounterGetCounterCheckIfCounterRetrievedWasUpdated() = runTest {
        val counterToInsert = Counter(name = "Counter")
        counterDAO.insertCounter(counterToInsert)
        val firstCounterFromDao = counterDAO.getCounter(1L)
        Assert.assertNotNull(firstCounterFromDao)
        Assert.assertTrue(firstCounterFromDao!!.name == "Counter")

        counterDAO.updateCounter(firstCounterFromDao.copy(value = 5L))
        val secondCounterFromDao = counterDAO.getCounter(1L)
        Assert.assertNotNull(secondCounterFromDao)
        Assert.assertTrue(secondCounterFromDao!!.name == "Counter")
        Assert.assertTrue(secondCounterFromDao.value == 5L)

        counterDAO.getCounters().test {
            Assert.assertTrue(awaitItem().size == 1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertTwoParentCountersAndOneChildCounterGetParentCountersCheckListSizeIsTwo() = runTest {
        val parentCounterOne = Counter(name = "Parent One")
        val parentCounterTwo = Counter(name = "Parent Two")
        val childCounterOne = Counter(name = "Child Counter", parentId = 1L)

        counterDAO.insertCounter(parentCounterOne)
        counterDAO.insertCounter(parentCounterTwo)
        counterDAO.insertCounter(childCounterOne)

        counterDAO.getParentCounters().test {
            Assert.assertTrue(awaitItem().size == 2)
            cancelAndIgnoreRemainingEvents()
        }

        counterDAO.getCounters().test {
            Assert.assertTrue(awaitItem().size == 3)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertCounterGetCounterCheckIfEqual() = runTest {
        val counterToInsert = Counter(name = "Counter One", value = 10L)
        counterDAO.insertCounter(counterToInsert)
        val counterFromDB = counterDAO.getCounter(1L)
        Assert.assertTrue(counterFromDB != null)
        Assert.assertTrue(counterFromDB!!.name == "Counter One")
        Assert.assertTrue(counterFromDB.value == 10L)
    }

    @Test
    fun insertCountersGetCounterListCheckNotEmpty() = runTest {
        val counter = Counter(name = "Test", value = 1L)
        counterDAO.insertCounter(counter)
        counterDAO.insertCounter(counter)
        counterDAO.getCounters().test {
            Assert.assertTrue(awaitItem().isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}