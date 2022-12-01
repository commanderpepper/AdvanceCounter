package room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.commanderpepper.advancecounter.database.model.Counter
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDAO {
    @Query("SELECT * FROM counter")
    fun getCounters(): Flow<List<Counter>>

    @Insert
    suspend fun insertCounter(counter: Counter)
}