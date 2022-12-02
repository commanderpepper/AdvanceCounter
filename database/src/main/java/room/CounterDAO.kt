package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.commanderpepper.advancecounter.database.model.Counter
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counter")
    fun getCounters(): Flow<List<Counter>>

    @Insert
    suspend fun insertCounter(counter: Counter)

    @Query("SELECT * FROM counter WHERE id = :counterId")
    suspend fun getCounter(counterId: Long): Counter?

    @Update
    suspend fun updateCounter(counter: Counter)
}