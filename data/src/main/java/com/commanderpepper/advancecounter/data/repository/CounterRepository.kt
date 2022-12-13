package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.model.repo.CounterRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface CounterRepository {
    fun getParentCounters(): Flow<List<CounterRepo>>

    fun getChildCounters(parentId: Long): Flow<List<CounterRepo>>

    fun getCounterFlow(counterId: Long): Flow<CounterRepo>

    suspend fun getCounter(counterId: Long): CounterRepo

    suspend fun insertCounter(counterRepo: CounterRepo)

    suspend fun updateCounter(counterRepo: CounterRepo)

    suspend fun editCounter(counterId: Long, newCounterName: String, newCounterStep: Long, newCounterValue: Long, newCounterThreshold: Long)

    fun incrementCounter(coroutineScope: CoroutineScope, counterId: Long)

    fun decrementCounter(coroutineScope: CoroutineScope, counterId: Long)

    suspend fun deleteCounter(counterId: Long)
}