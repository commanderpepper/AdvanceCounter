package com.commanderpepper.advancecounter.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param id ID of the counter, auto generated by Room
 * @param name name of the counter
 * @param value value of the counter
 * @param step value that the counter changes when the user increment / decrements
 * @param threshold value used to calculate next upper and lower threshold
 * @param upperThreshold next upper value that will trigger an event informing other counters
 * @param lowerThreshold next lower value that will trigger an event informing other counters
 * @param parentId parent ID of the counter, if null then the counter is a parent
 * @param relationship when the relationship is 1 then a counter can inform its children, when 2 the child can inform its parent
 */
@Entity
data class Counter (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(defaultValue = "0")
    val value: Long = 0,
    val step: Long = 1,
    val threshold: Long = 1,
    val upperThreshold: Long = 1,
    val lowerThreshold: Long = -1,
    val parentId: Long? = null,
    @ColumnInfo(defaultValue = "1")
    val relationship: Long = 1
)