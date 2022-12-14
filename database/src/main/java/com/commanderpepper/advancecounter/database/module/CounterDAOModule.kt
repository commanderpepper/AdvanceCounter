package com.commanderpepper.advancecounter.database.module

import android.content.Context
import androidx.room.Room
import com.commanderpepper.advancecounter.database.room.CounterDatabase
import com.commanderpepper.advancecounter.database.room.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterDAOModule {

    @Singleton
    @Provides
    fun provideCounterDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CounterDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesCounterDAO(counterDatabase: CounterDatabase) = counterDatabase.counterDao()
}