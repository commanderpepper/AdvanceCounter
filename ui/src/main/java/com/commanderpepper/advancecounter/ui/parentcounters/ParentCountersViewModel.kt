package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.ui.addcounterdialog.AddCounterState
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import com.commanderpepper.advancecounter.usecase.ConvertAddCounterStateToCounterRepoUseCase
import com.commanderpepper.advancecounter.usecase.ConvertCounterRepoToCounterItemUIStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentCountersViewModel @Inject constructor(
    private val counterRepository: CounterRepository,
    private val convertCounterRepoToCounterItemUIStateUseCase: ConvertCounterRepoToCounterItemUIStateUseCase,
    private val convertAddCounterStateToCounterRepoUseCase: ConvertAddCounterStateToCounterRepoUseCase
    ): ViewModel() {
    val parentCounters = counterRepository.getParentCounters().map { counterRepoList ->
        counterRepoList.map { counterRepo ->
            convertCounterRepoToCounterItemUIStateUseCase(counterRepo)
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
                convertAddCounterStateToCounterRepoUseCase(addCounterState = addCounterState, parentId = null)
            )
        }
    }
}