package com.commanderpepper.advancecounter.model.repo

data class CounterRepo (
    val id: Long,
    val name: String,
    val value: Long,
    val step: Long,
    val threshold: Long,
    val upperThreshold: Long,
    val lowerThreshold: Long,
    val parentId: Long?,
    val relationship: Long
)