package com.commanderpepper.advancecounter.data

import app.cash.turbine.test
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.data.repository.CounterRepositoryImpl
import com.commanderpepper.advancecounter.database.model.Counter
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
import room.CounterDAO

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CounterRepositoryTest {

    @Mock
    private lateinit var counterDao: CounterDAO

    private lateinit var counterRepository: CounterRepository

    @Before
    fun setUpMocks(){
        counterDao = mock(CounterDAO::class.java)
        counterRepository = CounterRepositoryImpl(counterDao)
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
}