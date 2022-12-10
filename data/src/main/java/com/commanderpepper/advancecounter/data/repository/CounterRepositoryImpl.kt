package com.commanderpepper.advancecounter.data.repository

import com.commanderpepper.advancecounter.database.room.CounterDAO
import com.commanderpepper.advancecounter.model.repo.CounterRepo
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterToCounterRepoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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

    override fun getCounterFlow(counterId: Long): Flow<CounterRepo> {
        return counterDAO.getCounterFlow(counterId).map {
            convertCounterToCounterRepoUseCase(it)
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

    override suspend fun editCounter(counterId: Long, newCounterName: String, newCounterStep: Long, newCounterValue: Long, newCounterThreshold: Long) {
        val counterRepo = getCounter(counterId).copy(
            name = newCounterName,
            step = newCounterStep,
            value = newCounterValue,
            threshold = newCounterThreshold,
            lowerThreshold = newCounterValue - newCounterThreshold,
            upperThreshold = newCounterValue + newCounterThreshold)
        updateCounter(counterRepo)
    }

    override suspend fun incrementCounter(counterId: Long) {
        val counter = getCounter(counterId)
        if(counter.relationship == 1L){
            incrementCounterParentToChild(counterId)
        }
        else {
            incrementCounterChildToParent(counterId)
        }
    }

    override suspend fun decrementCounter(counterId: Long) {
        val counter = getCounter(counterId)
        if(counter.relationship == 1L){
            decrementCounterParentToChild(counterId)
        }
        else {
            decrementCounterChildToParent(counterId)
        }
    }

    override suspend fun incrementCounterParentToChild(counterId: Long) {
        val current = getCounter(counterId)
        val nextValue = current.value + current.step
        // Inform others counters if true
        if(nextValue >= current.upperThreshold){
            var currentUpperThreshold = current.upperThreshold
            var currentLowerThreshold = current.lowerThreshold

            while(currentUpperThreshold <= nextValue){
                currentUpperThreshold += current.threshold
                currentLowerThreshold += current.threshold

                val childCounterIds = counterDAO.getChildCounterList(counterId)?.map { it.id }
                if(childCounterIds != null && childCounterIds.isNotEmpty()){
                    incrementCounters(childCounterIds)
                }

            }

            updateCounter(current.copy(value = nextValue, upperThreshold = currentUpperThreshold, lowerThreshold = currentLowerThreshold))
        }
        else {
            updateCounter(current.copy(value = nextValue))
        }
    }

    override suspend fun decrementCounterParentToChild(counterId: Long) {
        val current = getCounter(counterId)
        val nextValue = current.value - current.step
        // Inform others counters if true
        if(nextValue <= current.lowerThreshold){
            var currentUpperThreshold = current.upperThreshold
            var currentLowerThreshold = current.lowerThreshold

            while(currentLowerThreshold >= nextValue){
                currentUpperThreshold -= current.threshold
                currentLowerThreshold -= current.threshold

                val childCounterIds = counterDAO.getChildCounterList(counterId)?.map { it.id }
                if(childCounterIds != null && childCounterIds.isNotEmpty()){
                    decrementCounters(childCounterIds)
                }
            }

            updateCounter(current.copy(value = nextValue, lowerThreshold = currentLowerThreshold, upperThreshold = currentUpperThreshold))
        }
        else {
            updateCounter(current.copy(value = nextValue))
        }
    }

    override suspend fun incrementCounterChildToParent(counterId: Long) {
        val current = getCounter(counterId)
        val nextValue = current.value + current.step
        // Inform parent counters if true
        if(nextValue >= current.upperThreshold){
            var currentUpperThreshold = current.upperThreshold
            var currentLowerThreshold = current.lowerThreshold

            while(currentUpperThreshold <= nextValue){
                currentUpperThreshold += current.threshold
                currentLowerThreshold += current.threshold

                if(current.parentId != null){
                    val parentCounterRepo = getCounter(current.parentId!!)
                    incrementCounterChildToParent(parentCounterRepo.id)
                }
            }

            updateCounter(current.copy(value = nextValue, upperThreshold = currentUpperThreshold, lowerThreshold = currentLowerThreshold))
        }
        else {
            updateCounter(current.copy(value = nextValue))
        }
    }

    override suspend fun decrementCounterChildToParent(counterId: Long) {
        val current = getCounter(counterId)
        val nextValue = current.value - current.step
        // Inform parent counters if true
        if(nextValue <= current.lowerThreshold){
            var currentUpperThreshold = current.upperThreshold
            var currentLowerThreshold = current.lowerThreshold

            while(currentLowerThreshold >= nextValue){
                currentUpperThreshold -= current.threshold
                currentLowerThreshold -= current.threshold

                if(current.parentId != null){
                    val parentCounterRepo = getCounter(current.parentId!!)
                    decrementCounterChildToParent(parentCounterRepo.id)
                }
            }

            updateCounter(current.copy(value = nextValue, lowerThreshold = currentLowerThreshold, upperThreshold = currentUpperThreshold))
        }
        else {
            updateCounter(current.copy(value = nextValue))
        }
    }

    override suspend fun deleteCounter(counterId: Long) {
        val counter = counterDAO.getCounter(counterId)
        if (counter != null){
            val childCounters = counterDAO.getChildCounterList(counterId)
            if(childCounters != null && childCounters.isNotEmpty()){
                childCounters.forEach {
                    deleteCounter(it.id)
                }
            }
            counterDAO.deleteCounter(counter)
        }
    }

    private suspend fun incrementCounters(counterIdList: List<Long>){
        counterIdList.forEach { id ->
            incrementCounterParentToChild(id)
        }
    }

    private suspend fun decrementCounters(counterIdList: List<Long>){
        counterIdList.forEach { id ->
            decrementCounterParentToChild(id)
        }
    }

}