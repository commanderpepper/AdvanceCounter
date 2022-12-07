package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.database.model.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.commanderpepper.advancecounter.database.room.CounterDAO
import javax.inject.Inject
import kotlin.math.abs

class CounterRepositoryImpl @Inject constructor(private val counterDAO: CounterDAO): CounterRepository {
    override fun getParentCounters(): Flow<List<CounterRepo>> {
        return counterDAO.getParentCounters().map {
            it.map { counter ->
                CounterRepo(
                    id = counter.id,
                    name = counter.name,
                    value = counter.value,
                    step = counter.step,
                    threshold = counter.threshold,
                    upperThreshold = counter.upperThreshold,
                    lowerThreshold = counter.lowerThreshold,
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
                    value = counter.value,
                    step = counter.step,
                    threshold = counter.threshold,
                    upperThreshold = counter.upperThreshold,
                    lowerThreshold = counter.lowerThreshold,
                    parentId = counter.parentId
                )
            }
        }
    }

    override suspend fun getCounter(counterId: Long): CounterRepo {
        val counter = counterDAO.getCounter(counterId)!!
        return CounterRepo(
            id = counter.id,
            name = counter.name,
            value = counter.value,
            step = counter.step,
            threshold = counter.threshold,
            upperThreshold = counter.upperThreshold,
            lowerThreshold = counter.lowerThreshold,
            parentId = counter.parentId
        )
    }

    override suspend fun insertCounter(counterRepo: CounterRepo) {
        counterDAO.insertCounter(
            Counter(
                name = counterRepo.name,
                value = counterRepo.value,
                step = counterRepo.step,
                threshold = counterRepo.threshold,
                upperThreshold = counterRepo.upperThreshold,
                lowerThreshold = counterRepo.lowerThreshold,
                parentId = counterRepo.parentId)
        )
    }

    override suspend fun updateCounter(counterRepo: CounterRepo) {
        counterDAO.updateCounter(
            Counter(
                id = counterRepo.id,
                name = counterRepo.name,
                value = counterRepo.value,
                step = counterRepo.step,
                threshold = counterRepo.threshold,
                upperThreshold = counterRepo.upperThreshold,
                lowerThreshold = counterRepo.lowerThreshold,
                parentId = counterRepo.parentId)
        )
    }

    override suspend fun incrementCounter(counterId: Long) {
        val current = getCounter(counterId)
        val nextValue = current.value + current.step
        // Inform others counters if true
        if(nextValue >= current.upperThreshold){
            var currentUpperThreshold = current.upperThreshold
            var currentLowerThreshold = current.lowerThreshold

            while(currentUpperThreshold <= nextValue){
                currentUpperThreshold += current.threshold
                currentLowerThreshold += current.threshold
            }

            updateCounter(current.copy(value = nextValue, upperThreshold = currentUpperThreshold, lowerThreshold = currentLowerThreshold))
        }
        else {
            updateCounter(current.copy(value = nextValue))
        }
    }

    override suspend fun decrementCounter(counterId: Long) {
        val current = getCounter(counterId)
        val nextValue = current.value - current.step
        // Inform others counters if true
        if(nextValue <= current.lowerThreshold){
            var currentUpperThreshold = current.upperThreshold
            var currentLowerThreshold = current.lowerThreshold

            while(currentLowerThreshold >= nextValue){
                currentUpperThreshold -= current.threshold
                currentLowerThreshold -= current.threshold
            }

            updateCounter(current.copy(value = nextValue, lowerThreshold = currentLowerThreshold, upperThreshold = currentUpperThreshold))
        }
        else {
            updateCounter(current.copy(value = nextValue))
        }
    }

}