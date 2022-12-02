package com.commanderpepper.advancecounter.data.model

data class CounterRepo(
    val id: Long,
    val name: String,
    val value: String,
    val parentId: Long?
)