package com.commanderpepper.advancecounter.data

import app.cash.turbine.test
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.data.repository.CounterRepositoryImpl
import com.commanderpepper.advancecounter.model.database.Counter
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterToCounterRepoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import com.commanderpepper.advancecounter.database.room.CounterDAO

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CounterRepositoryTest {

    @Mock
    private lateinit var counterDao: CounterDAO

    private lateinit var counterRepository: CounterRepository

    @Before
    fun setUpMocks() {
        counterDao = mock(CounterDAO::class.java)
        counterRepository = CounterRepositoryImpl(counterDao, ConvertCounterToCounterRepoUseCase(), ConvertCounterRepoToCounterUseCase())
    }

    @Test
    fun `receive a flow of parent counter check that list size is one`() = runTest {
        Mockito.`when`(counterDao.getParentCounters()).thenReturn(flow {
            emit(listOf(Counter(0, "", 0)))
        })

        counterRepository.getParentCounters().test {
            Assert.assertTrue(awaitItem().size == 1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `receive a flow of child counters check that list size is two`() = runTest {
        Mockito.`when`(counterDao.getChildCounters(1L)).thenReturn(flow {
            emit(listOf(
                Counter(0, "", 0, 1L),
                Counter(1, "", 0, 1L)
            ))
        })

        counterRepository.getChildCounters(1L).test {
            Assert.assertTrue(awaitItem().size == 2)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `receive empty of counters and check that counter repo works`() = runTest {
        Mockito.`when`(counterDao.getParentCounters()).thenReturn(null)

        counterRepository.getParentCounters().test {
            Assert.assertTrue(awaitItem().isNullOrEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}