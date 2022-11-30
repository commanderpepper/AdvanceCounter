package com.commanderpepper.advancecounter.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Counter (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val value: Long
)