package com.commanderpepper.advancecounter.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.commanderpepper.advancecounter.model.database.Counter
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counter")
    fun getCounters(): Flow<List<Counter>>

    @Query("SELECT * FROM counter WHERE parentId IS NULL")
    fun getParentCounters(): Flow<List<Counter>>

    @Query("SELECT * FROM counter WHERE parentId = :parentCounterId")
    fun getChildCounters(parentCounterId: Long): Flow<List<Counter>>

    @Query("SELECT * FROM counter WHERE id = :counterId")
    fun getCounterFlow(counterId: Long): Flow<Counter>

    @Query("SELECT * FROM counter WHERE parentId = :parentCounterId")
    suspend fun getChildCounterList(parentCounterId: Long): List<Counter>?

    @Insert
    suspend fun insertCounter(counter: Counter)

    @Query("SELECT * FROM counter WHERE id = :counterId")
    suspend fun getCounter(counterId: Long): Counter?

    @Update
    suspend fun updateCounter(counter: Counter)
}