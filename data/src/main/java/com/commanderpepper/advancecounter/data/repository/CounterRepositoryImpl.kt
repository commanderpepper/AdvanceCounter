package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.database.model.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.commanderpepper.advancecounter.database.room.CounterDAO
import javax.inject.Inject

class CounterRepositoryImpl @Inject constructor(private val counterDAO: CounterDAO): CounterRepository {
    override fun getParentCounters(): Flow<List<CounterRepo>> {
        return counterDAO.getParentCounters().map {
            it.map { counter ->
                CounterRepo(
                    id = counter.id,
                    name = counter.name,
                    value = counter.value.toString(),
                    parentId = counter.parentId
                )
            }
        }
    }

    override fun getChildCounters(parentId: Long): Flow<List<CounterRepo>> {
        return counterDAO.getChildCounters(parentId).map {
            it.map { counter ->
                CounterRepo (
                    id = counter.id,
                    name = counter.name,
                    value = counter.value.toString(),
                    parentId = counter.parentId
                )
            }
        }
    }

    override suspend fun insertCounter(counterRepo: CounterRepo) {
        counterDAO.insertCounter(
            Counter(
                name = counterRepo.name,
                value = counterRepo.value.toLong(),
                parentId = counterRepo.parentId)
        )
    }

    override suspend fun updateCounter(counterRepo: CounterRepo) {
        counterDAO.updateCounter(
            Counter(
                id = counterRepo.id,
                name = counterRepo.name,
                value = counterRepo.value.toLong(),
                parentId = counterRepo.parentId)
        )
    }

}