package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.ui.addcounterdialog.AddCounterState
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentCountersViewModel @Inject constructor(private val counterRepository: CounterRepository): ViewModel() {
    val parentCounters = counterRepository.getParentCounters().map { counterRepoList ->
        counterRepoList.map { counterRepo ->
            CounterItemUIState(
                id = counterRepo.id,
                name = counterRepo.name,
                value = counterRepo.value.toString(),
                lowerThreshold = counterRepo.lowerThreshold.toString(),
                upperThreshold = counterRepo.upperThreshold.toString()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun plusButtonOnClick(parentCounterId: Long){
        viewModelScope.launch {
            counterRepository.incrementCounter(parentCounterId)
        }
    }

    fun minusButtonOnClick(parentCounterId: Long){
        viewModelScope.launch {
            counterRepository.decrementCounter(parentCounterId)
        }
    }

    fun addNewParentCounter(addCounterState: AddCounterState){
        viewModelScope.launch {
            counterRepository.insertCounter(
                CounterRepo(
                    id = 0L,
                    name = addCounterState.name.ifEmpty { "Counter" },
                    value = addCounterState.value,
                    step = addCounterState.step,
                    threshold = addCounterState.threshold,
                    upperThreshold = addCounterState.value + addCounterState.threshold,
                    lowerThreshold = addCounterState.value - addCounterState.threshold,
                    parentId = null
                )
            )
        }
    }
}