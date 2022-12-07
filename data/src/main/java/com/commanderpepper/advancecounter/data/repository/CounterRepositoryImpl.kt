package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.database.model.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.commanderpepper.advancecounter.database.room.CounterDAO
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterToCounterRepoUseCase
import javax.inject.Inject
import kotlin.math.abs

class CounterRepositoryImpl @Inject constructor(
    private val counterDAO: CounterDAO,
    private val convertCounterToCounterRepoUseCase: ConvertCounterToCounterRepoUseCase,
    private val convertCounterRepoToCounterUseCase: ConvertCounterRepoToCounterUseCase
    ): CounterRepository {

    override fun getParentCounters(): Flow<List<CounterRepo>> {
        return counterDAO.getParentCounters().map {
            it.map { counter ->
                convertCounterToCounterRepoUseCase(counter)
            }
        }
    }

    override fun getChildCounters(parentId: Long): Flow<List<CounterRepo>> {
        return counterDAO.getChildCounters(parentId).map {
            it.map { counter ->
                convertCounterToCounterRepoUseCase(counter)
            }
        }
    }

    override suspend fun getCounter(counterId: Long): CounterRepo {
        val counter = counterDAO.getCounter(counterId)!!
        return convertCounterToCounterRepoUseCase(counter)
    }

    override suspend fun insertCounter(counterRepo: CounterRepo) {
        counterDAO.insertCounter(convertCounterRepoToCounterUseCase(counterRepo))
    }

    override suspend fun updateCounter(counterRepo: CounterRepo) {
        counterDAO.updateCounter(convertCounterRepoToCounterUseCase(counterRepo))
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