package com.commanderpepper.advancecounter.data.model

data class CounterRepo(
    val id: Long,
    val name: String,
    val value: Long,
    val step: Long,
    val parentId: Long?
)