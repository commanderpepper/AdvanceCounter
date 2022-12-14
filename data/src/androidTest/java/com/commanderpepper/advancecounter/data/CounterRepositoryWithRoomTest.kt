package com.commanderpepper.advancecounter.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.data.repository.CounterRepositoryImpl
import com.commanderpepper.advancecounter.database.room.CounterDAO
import com.commanderpepper.advancecounter.database.room.CounterDatabase
import com.commanderpepper.advancecounter.model.repo.CounterRepo
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterToCounterRepoUseCase
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
        counterRepository = CounterRepositoryImpl(counterDAO, ConvertCounterToCounterRepoUseCase(), ConvertCounterRepoToCounterUseCase())
    }

    @After
    @Throws(IOException::class)
    fun closeResources() {
        database.close()
    }

    @Test
    fun insertMultipleCountersGetFlowCheckFlow() = runTest {
        val counterRepoList = List<CounterRepo>(3){
            CounterRepo(
                id = 0,
                name = "Counter Repo $it",
                value = 0,
                step = 1,
                threshold = 2,
                upperThreshold = 1,
                lowerThreshold = 1,
                null,
                relationship = 1
            )
        }
        counterRepoList.forEach {
            counterRepository.insertCounter(it)
        }
        val parents = counterRepository.getParentCounters()
        parents.test {
            Assert.assertEquals(3, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertCounterEditCounterCheckCounter() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Counter Repo",
                value = 0,
                step = 1,
                threshold = 2,
                upperThreshold = 1,
                lowerThreshold = 1,
                null,
                relationship = 1
            )
        )
        counterRepository.editCounter(1L, "Test", 1, 1, 2)
        val updatedCounter = counterRepository.getCounter(1L)
        Assert.assertEquals("Test", updatedCounter.name)
    }

    @Test
    fun insertCountersDeleteOneCounterCheckForOneCounter() = runTest {
        repeat(2){
            counterRepository.insertCounter(
                CounterRepo(
                    id = 0,
                    name = "Counter Repo",
                    value = 0,
                    step = 1,
                    threshold = 2,
                    upperThreshold = 1,
                    lowerThreshold = 1,
                    null,
                    relationship = 1
                )
            )
        }
        val beforeCounterList = counterRepository.getParentCounters()
        beforeCounterList.test {
            Assert.assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
        counterRepository.deleteCounter(1L)
        val afterCounterList = counterRepository.getParentCounters()
        afterCounterList.test {
            Assert.assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertCountersWithChildDeleteOneParentCheckForNoCounters() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Counter Repo",
                value = 0,
                step = 1,
                threshold = 2,
                upperThreshold = 1,
                lowerThreshold = 1,
                null,
                relationship = 1
            )
        )

        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Counter Repo",
                value = 0,
                step = 1,
                threshold = 2,
                upperThreshold = 1,
                lowerThreshold = 1,
                1,
                relationship = 1
            )
        )

        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Counter Repo",
                value = 0,
                step = 1,
                threshold = 2,
                upperThreshold = 1,
                lowerThreshold = 1,
                1,
                relationship = 1
            )
        )

        val beforeCounterParentList = counterRepository.getParentCounters()
        beforeCounterParentList.test {
            Assert.assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
        val beforeChildrenList = counterRepository.getChildCounters(1L)
        beforeChildrenList.test {
            Assert.assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }

        counterRepository.deleteCounter(1L)

        val afterCounterParentList = counterRepository.getParentCounters()
        afterCounterParentList.test {
            Assert.assertTrue(awaitItem().isNullOrEmpty())
            cancelAndIgnoreRemainingEvents()
        }
        val afterChildrenList = counterRepository.getChildCounters(1L)
        afterChildrenList.test {
            Assert.assertTrue(awaitItem().isNullOrEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insertOneCounterGetCounterRepoFlow() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Counter Repo",
                value = 0,
                step = 1,
                threshold = 2,
                upperThreshold = 1,
                lowerThreshold = 1,
                null,
                relationship = 1
            )
        )
        val returnedCounter = counterRepository.getCounterFlow(1)
        returnedCounter.test {
            Assert.assertEquals("Counter Repo", awaitItem().name)
            cancelAndIgnoreRemainingEvents()
        }
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
                threshold = 10L,
                upperThreshold = 10L,
                lowerThreshold = -10L,
                relationship = 1
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
                threshold = 10L,
                upperThreshold = 10L,
                lowerThreshold = -10L,
                relationship = 1
            )
        )
        repeat(10) {
            counterRepository.incrementCounter(this, 1L)
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
                threshold = 2L,
                upperThreshold = 2L,
                lowerThreshold = -2L,
                relationship = 1
            )
        )
        counterRepository.incrementCounter(this, 1L)
        Assert.assertEquals(6L, counterRepository.getCounter(1L).upperThreshold)
        Assert.assertEquals(2L, counterRepository.getCounter(1L).lowerThreshold)
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
                threshold = 2L,
                upperThreshold = 2L,
                lowerThreshold = -2L,
                relationship = 1
            )
        )
        counterRepository.decrementCounter(this, 1L)
        Assert.assertEquals(-6L, counterRepository.getCounter(1L).lowerThreshold)
        Assert.assertEquals(-2L, counterRepository.getCounter(1L).upperThreshold)
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
                threshold = 2L,
                upperThreshold = 2L,
                lowerThreshold = -2L,
                relationship = 1
            )
        )
        counterRepository.incrementCounter(this, 1L)
        Assert.assertEquals(2L, counterRepository.getCounter(1L).upperThreshold)
        Assert.assertEquals(-2L, counterRepository.getCounter(1L).lowerThreshold)
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
                threshold = 2L,
                upperThreshold = 2L,
                lowerThreshold = -2L,
                relationship = 1
            )
        )
        counterRepository.decrementCounter(this, 1L)
        Assert.assertEquals(2L, counterRepository.getCounter(1L).upperThreshold)
        Assert.assertEquals(-2L, counterRepository.getCounter(1L).lowerThreshold)
    }

    @Test
    fun insertCounterWhereStepIsSmallerThanThresholdThenIncrementFiveTimesCheckThreshold() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 1,
                threshold = 5L,
                upperThreshold = 5L,
                lowerThreshold = -5L,
                relationship = 1
            )
        )
        repeat(5) {
            counterRepository.incrementCounter(this, 1L)
        }
        Assert.assertEquals(10L, counterRepository.getCounter(1L).upperThreshold)
        Assert.assertEquals(0L, counterRepository.getCounter(1L).lowerThreshold)
    }

    @Test
    fun insertCounterWhereStepIsSmallerThanThresholdThenDecrementFiveTimesCheckThreshold() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0L,
                name = "Test",
                value = 0L,
                parentId = null,
                step = 1,
                threshold = 5L,
                upperThreshold = 5L,
                lowerThreshold = -5L,
                relationship = 1
            )
        )
        repeat(5) {
            counterRepository.decrementCounter(this,1L)
        }
        Assert.assertEquals(0L, counterRepository.getCounter(1L).upperThreshold)
        Assert.assertEquals(-10L, counterRepository.getCounter(1L).lowerThreshold)
    }

    @Test
    fun insertNoCountersGetCountersCheckForNoCounters() = runTest {
        val counterFlow = counterRepository.getParentCounters()
        counterFlow.test {
            Assert.assertEquals(0, awaitItem().size)
        }
    }

    @Test
    fun insertParentCounterAndChildCounterThenIncrementParentViaChildCheckForCorrectValue() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Parent Counter Repo",
                value = 0,
                step = 2,
                threshold = 1,
                upperThreshold = 1,
                lowerThreshold = -1,
                null,
                relationship = 1
            )
        )
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Counter Repo",
                value = 0,
                step = 2,
                threshold = 1,
                upperThreshold = 1,
                lowerThreshold = -1,
                parentId = 1,
                relationship = 1
            )
        )

        counterRepository.incrementCounter(this,2)

        val childCounter = counterRepository.getCounter(2L)
        val parentCounter = counterRepository.getCounter(1L)


        Assert.assertEquals(2, childCounter.value)
        Assert.assertEquals(4, parentCounter.value)
    }

    @Test
    fun insertMultipleCountersThenIncrementLeafChildCheckParentsHaveCorrectValues() = runTest {
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Parent Counter Repo",
                value = 0,
                step = 2,
                threshold = 1,
                upperThreshold = 1,
                lowerThreshold = -1,
                null,
                relationship = 1
            )
        )
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Child Counter",
                value = 0,
                step = 2,
                threshold = 1,
                upperThreshold = 1,
                lowerThreshold = -1,
                parentId = 1,
                relationship = 1
            )
        )
        counterRepository.insertCounter(
            CounterRepo(
                id = 0,
                name = "Child Counter Leaf",
                value = 0,
                step = 2,
                threshold = 1,
                upperThreshold = 1,
                lowerThreshold = -1,
                parentId = 2,
                relationship = 1
            )
        )

        counterRepository.incrementCounter(this, 3L)

        val parentCounter = counterRepository.getCounter(1L)
        val childCounterMiddle = counterRepository.getCounter(2L)
        val childCounterLeaf = counterRepository.getCounter(3L)

        Assert.assertEquals(2, childCounterLeaf.value)
        Assert.assertEquals(4, childCounterMiddle.value)
        Assert.assertEquals(8, parentCounter.value)
    }
}