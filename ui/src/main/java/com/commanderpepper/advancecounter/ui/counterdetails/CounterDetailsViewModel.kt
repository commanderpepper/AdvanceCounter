package com.commanderpepper.advancecounter.ui.counterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class CounterDetailsViewModel(
    private val counterRepository: CounterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val childCounters = counterRepository.getChildCounters(savedStateHandle.get<Long>("parentID")!!).map { list ->
        list.map { counterRepo ->
            CounterItemUIState(
                id = counterRepo.id,
                name = counterRepo.name,
                value = counterRepo.value.toString()
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _parentCounter = MutableStateFlow<CounterItemUIState>(CounterItemUIState(1, "", ""))
    val parentCounter: StateFlow<CounterItemUIState> = _parentCounter

    init {
        viewModelScope.launch {
            val parentCounter =
                counterRepository.getCounter(savedStateHandle.get<Long>("parentID")!!)
            _parentCounter.emit(
                CounterItemUIState(
                    id = parentCounter.id,
                    name = parentCounter.name,
                    value = parentCounter.value.toString()
                )
            )
        }
    }
}