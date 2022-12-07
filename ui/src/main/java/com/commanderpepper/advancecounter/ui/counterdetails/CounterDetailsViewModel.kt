package com.commanderpepper.advancecounter.ui.counterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.model.ui.AddCounterState
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState
import com.commanderpepper.advancecounter.usecase.ConvertAddCounterStateToCounterRepoUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterItemUIStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterDetailsViewModel @Inject constructor(
    private val counterRepository: CounterRepository,
    private val savedStateHandle: SavedStateHandle,
    private val convertCounterRepoToCounterItemUIState: ConvertCounterRepoToCounterItemUIStateUseCase,
    private val convertAddCounterStateToCounterRepoUseCase: ConvertAddCounterStateToCounterRepoUseCase
) : ViewModel() {

    val childCounters =
        counterRepository.getChildCounters(savedStateHandle.get<String>("counterId")!!.toLong())
            .map { list ->
                list.map { counterRepo ->
                    convertCounterRepoToCounterItemUIState(counterRepo)
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _parentCounter =
        MutableStateFlow<CounterItemUIState>(CounterItemUIState(
            id = 1,
            name = "",
            value = "",
            step = "",
            lowerThreshold = "",
            upperThreshold = ""
        ))
    val parentCounter: StateFlow<CounterItemUIState> = _parentCounter

    init {
        viewModelScope.launch {
            val parentCounter =
                counterRepository.getCounter(savedStateHandle.get<String>("counterId")!!.toLong())
            _parentCounter.emit(
                convertCounterRepoToCounterItemUIState(parentCounter)
            )
        }
    }

    fun addCounter(addCounterState: AddCounterState) {
        viewModelScope.launch {
            counterRepository.insertCounter(
                convertAddCounterStateToCounterRepoUseCase(
                    addCounterState,
                    savedStateHandle.get<String>("counterId")!!.toLong()
                )
            )
        }
    }
}