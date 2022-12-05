package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.data.model.CounterRepo
import kotlinx.coroutines.flow.Flow

interface CounterRepository {
    fun getParentCounters(): Flow<List<CounterRepo>>

    fun getChildCounters(parentId: Long): Flow<List<CounterRepo>>

    suspend fun getCounter(counterId: Long): CounterRepo

    suspend fun insertCounter(counterRepo: CounterRepo)

    suspend fun updateCounter(counterRepo: CounterRepo)

    suspend fun incrementCounter(counterId: Long)

    suspend fun decrementCounter(counterId: Long)
}