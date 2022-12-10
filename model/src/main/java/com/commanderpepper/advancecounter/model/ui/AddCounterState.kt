package com.commanderpepper.advancecounter.model.ui

data class AddCounterState(
    val name: String,
    val value: Long,
    val step: Long = 1,
    val threshold: Long,
    val relationship: Long = 1
)
