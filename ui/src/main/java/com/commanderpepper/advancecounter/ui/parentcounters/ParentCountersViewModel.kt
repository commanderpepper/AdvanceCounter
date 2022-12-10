package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.model.ui.AddCounterState
import com.commanderpepper.advancecounter.model.ui.CounterListUIState
import com.commanderpepper.advancecounter.model.ui.editcounter.EditCounterState
import com.commanderpepper.advancecounter.usecase.ConvertAddCounterStateToCounterRepoUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterItemUIStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentCountersViewModel @Inject constructor(
    private val counterRepository: CounterRepository,
    private val convertCounterRepoToCounterItemUIStateUseCase: ConvertCounterRepoToCounterItemUIStateUseCase,
    private val convertAddCounterStateToCounterRepoUseCase: ConvertAddCounterStateToCounterRepoUseCase
) : ViewModel() {

    val parentCounterListUIState: StateFlow<CounterListUIState> =
        counterRepository.getParentCounters().map { list ->
            if (list.isEmpty()) {
                CounterListUIState.Error("Add a counter")
            } else {
                CounterListUIState.Success(list.map { counterRepo ->
                    convertCounterRepoToCounterItemUIStateUseCase(
                        counterRepo
                    )
                })
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CounterListUIState.Loading
        )

    fun plusButtonOnClick(parentCounterId: Long) {
        viewModelScope.launch {
            counterRepository.incrementCounterParentToChild(parentCounterId)
        }
    }

    fun minusButtonOnClick(parentCounterId: Long) {
        viewModelScope.launch {
            counterRepository.decrementCounterParentToChild(parentCounterId)
        }
    }

    fun addNewParentCounter(addCounterState: AddCounterState) {
        viewModelScope.launch {
            counterRepository.insertCounter(
                convertAddCounterStateToCounterRepoUseCase(
                    addCounterState = addCounterState,
                    parentId = null
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