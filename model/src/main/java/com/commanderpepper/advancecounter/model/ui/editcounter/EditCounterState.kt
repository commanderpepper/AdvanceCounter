package com.commanderpepper.advancecounter.model.ui.editcounter

data class EditCounterState(
    val counterId: Long,
    val counterName: String,
    val counterStep: Long,
    val counterValue: Long,
    val counterThreshold: Long
)
