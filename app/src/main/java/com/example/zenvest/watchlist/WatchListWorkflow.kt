package com.example.zenvest.watchlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

@Entity(tableName = "watchlist_items")
data class WatchlistItem(
    @PrimaryKey val symbol: String,
    val name: String,
    val price: String,
    val changePercentage: String?
)

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistItem)

    @Query("DELETE FROM watchlist_items WHERE symbol = :symbol")
    suspend fun removeFromWatchlistBySymbol(symbol: String)

    @Query("SELECT * FROM watchlist_items")
    fun getWatchlist(): Flow<List<WatchlistItem>>

    @Query("SELECT EXISTS(SELECT * FROM watchlist_items WHERE symbol = :symbol)")
    fun isInWatchlistFlow(symbol: String): Flow<Boolean>
}


class WatchlistRepository(private val dao: WatchlistDao) {
    val watchlist: Flow<List<WatchlistItem>> = dao.getWatchlist()

    suspend fun addToWatchlist(item: WatchlistItem) {
        dao.addToWatchlist(item)
    }

    suspend fun removeFromWatchlist(symbol: String) {
        dao.removeFromWatchlistBySymbol(symbol)
    }

    fun isInWatchlistFlow(symbol: String): Flow<Boolean> {
        return dao.isInWatchlistFlow(symbol)
    }
}

class WatchlistViewModel(private val repository: WatchlistRepository) : ViewModel() {
    val watchlist: Flow<List<WatchlistItem>> = repository.watchlist

    fun isInWatchlistFlow(symbol: String): Flow<Boolean> {
        return repository.isInWatchlistFlow(symbol)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
    }

    @Transaction
    suspend fun toggleWatchlist(item: WatchlistItem) {
        if (repository.isInWatchlistFlow(item.symbol).first()) {
            repository.removeFromWatchlist(item.symbol)
        } else {
            repository.addToWatchlist(item)
        }
    }
}

@Database(
    entities = [WatchlistItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zenvest_database"
                )
                    .fallbackToDestructiveMigration() // Handle schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class WatchlistViewModelFactory(private val repository: WatchlistRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WatchlistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
