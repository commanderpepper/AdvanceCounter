package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.repository.CounterRepository
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
                value = counterRepo.value.toString()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun plusButtonOnClick(parentCounterId: Long){
        viewModelScope.launch {
            val current = counterRepository.getCounter(parentCounterId)
            counterRepository.updateCounter(current.copy(value = (current.value + 1)))
        }
    }

    fun minusButtonOnClick(parentCounterId: Long){
        viewModelScope.launch {
            val current = counterRepository.getCounter(parentCounterId)
            counterRepository.updateCounter(current.copy(value = (current.value - 1)))
        }
    }
}