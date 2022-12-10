package com.commanderpepper.advancecounter.model.ui

sealed interface CounterListUIState {
    object Loading: CounterListUIState
    data class Error(val message: String): CounterListUIState
    data class Success(val list: List<CounterItemUIState>) : CounterListUIState
}