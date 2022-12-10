package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.model.repo.CounterRepo
import kotlinx.coroutines.flow.Flow

interface CounterRepository {
    fun getParentCounters(): Flow<List<CounterRepo>>

    fun getChildCounters(parentId: Long): Flow<List<CounterRepo>>

    fun getCounterFlow(counterId: Long): Flow<CounterRepo>

    suspend fun getCounter(counterId: Long): CounterRepo

    suspend fun insertCounter(counterRepo: CounterRepo)

    suspend fun updateCounter(counterRepo: CounterRepo)

    suspend fun editCounter(counterId: Long, newCounterName: String, newCounterStep: Long, newCounterValue: Long, newCounterThreshold: Long)

    suspend fun incrementCounter(counterId: Long)

    suspend fun decrementCounter(counterId: Long)

    suspend fun incrementCounterParentToChild(counterId: Long)

    suspend fun decrementCounterParentToChild(counterId: Long)

    suspend fun incrementCounterChildToParent(counterId: Long)

    suspend fun decrementCounterChildToParent(counterId: Long)

    suspend fun deleteCounter(counterId: Long)
}