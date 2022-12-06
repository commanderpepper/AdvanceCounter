package com.commanderpepper.advancecounter.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.data.repository.CounterRepositoryImpl
import com.commanderpepper.advancecounter.database.room.CounterDAO
import com.commanderpepper.advancecounter.database.room.CounterDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CounterRepositoryWithRoomTest {
    private lateinit var counterDAO: CounterDAO
    private lateinit var database: CounterDatabase
    private lateinit var counterRepository: CounterRepository

    @Before
    fun createResources() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            CounterDatabase::class.java
        ).build()
        counterDAO = database.counterDao()
        counterRepository = CounterRepositoryImpl(counterDAO)
    }

    @After
    @Throws(IOException::class)
    fun closeResources() {
        database.close()
    }

    @Test
    fun insertCounterAssertCounterFromRoomIsTheSame() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 1L,
                parentId = null,
                step = 1,
                upperThreshold = 10L,
                lowerThreshold = -10L
            )
        )
        val counterRepo = counterRepository.getCounter(1L)
        Assert.assertEquals("Test", counterRepo.name)
    }

    @Test
    fun insertCounterIncrementTenTimeCheckValueIsCorrect() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 1,
                upperThreshold = 10L,
                lowerThreshold = -10L
            )
        )
        repeat(10){
            counterRepository.incrementCounter(1L)
        }

        Assert.assertEquals(10L, counterRepository.getCounter(1L).value)
    }

    @Test
    fun insertCounterWhereStepIsLargerThanThresholdIncrementThenCheckThreshold() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 5,
                upperThreshold = 2L,
                lowerThreshold = -2L
            )
        )
        counterRepository.incrementCounter(1L)
        Assert.assertEquals(6L, counterRepository.getCounter(1L).upperThreshold)
    }

    @Test
    fun insertCounterWhereStepIsLargerThanThresholdDecrementThenCheckThreshold() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 5,
                upperThreshold = 2L,
                lowerThreshold = -2L
            )
        )
        counterRepository.decrementCounter(1L)
        Assert.assertEquals(-6L, counterRepository.getCounter(1L).lowerThreshold)
    }

    @Test
    fun insertCounterWhereStepIsSmallerThanThresholdThenIncrementThenCheckThreshold() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 1,
                upperThreshold = 2L,
                lowerThreshold = -2L
            )
        )
        counterRepository.incrementCounter(1L)
        Assert.assertEquals(2L, counterRepository.getCounter(1L).upperThreshold)
    }

    @Test
    fun insertCounterWhereStepIsSmallerThanThresholdThenDecrementThenCheckThreshold() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 1,
                upperThreshold = 2L,
                lowerThreshold = -2L
            )
        )
        counterRepository.decrementCounter(1L)
        Assert.assertEquals(-2L, counterRepository.getCounter(1L).lowerThreshold)
    }
}