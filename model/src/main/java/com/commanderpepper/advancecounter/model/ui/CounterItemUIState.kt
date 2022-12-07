package com.commanderpepper.advancecounter.model.ui

data class CounterItemUIState(
    val id: Long,
    val name: String,
    val value: String,
    val step: String,
    val lowerThreshold: String,
    val upperThreshold: String
)
