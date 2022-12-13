package com.commanderpepper.advancecounter.ui.counterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.model.ui.AddCounterState
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState
import com.commanderpepper.advancecounter.model.ui.CounterListUIState
import com.commanderpepper.advancecounter.model.ui.editcounter.EditCounterState
import com.commanderpepper.advancecounter.usecase.ConvertAddCounterStateToCounterRepoUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterItemUIStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class CounterDetailsViewModel @Inject constructor(
    private val counterRepository: CounterRepository,
    private val savedStateHandle: SavedStateHandle,
    private val convertCounterRepoToCounterItemUIState: ConvertCounterRepoToCounterItemUIStateUseCase,
    private val convertAddCounterStateToCounterRepoUseCase: ConvertAddCounterStateToCounterRepoUseCase
) : ViewModel() {

    private val lock = Mutex()
    private val parentId = savedStateHandle.get<String>("counterId")!!.toLong()

    val childCounterListUIState: StateFlow<CounterListUIState> = counterRepository
        .getChildCounters(parentId).map { list ->
            if (list.isNullOrEmpty()) {
                CounterListUIState.Error("Add a child counter")
            } else {
                CounterListUIState.Success(list.map { counterRepo ->
                    convertCounterRepoToCounterItemUIState(counterRepo)
                })
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), CounterListUIState.Loading)

    val parentCounter: StateFlow<CounterItemUIState> = counterRepository.getCounterFlow(parentId)
        .map { convertCounterRepoToCounterItemUIState(it) }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000L),
            CounterItemUIState(
                id = 0, "", "", "", "", "", "", 1L
            )
        )

    fun plusButtonOnClick(counterId: Long) {
        viewModelScope.launch {
            lock.withLock {
                counterRepository.incrementCounter(counterId)
            }
        }
    }

    fun minusButtonOnClick(counterId: Long) {
        viewModelScope.launch {
            lock.withLock {
                counterRepository.decrementCounter(counterId)
            }
        }
    }

    fun addCounter(addCounterState: AddCounterState) {
        viewModelScope.launch {
            counterRepository.insertCounter(
                convertAddCounterStateToCounterRepoUseCase(
                    addCounterState,
                    parentId
                )
            )
        }
    }

    fun editCounter(editCounterState: EditCounterState) {
        viewModelScope.launch {
            counterRepository.editCounter(
                counterId = editCounterState.counterId,
                newCounterName = editCounterState.counterName,
                newCounterStep = editCounterState.counterStep,
                newCounterValue = editCounterState.counterValue,
                newCounterThreshold = editCounterState.counterThreshold
            )
        }
    }

    fun deleteCounter(counterId: Long) {
        viewModelScope.launch {
            counterRepository.deleteCounter(counterId)
        }
    }
}