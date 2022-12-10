package com.commanderpepper.advancecounter.model.ui.editcounter

data class ExistingCounterState(
    val counterId: Long,
    val counterName: String,
    val counterStep: String,
    val counterValue: String,
    val counterThreshold: String
)
