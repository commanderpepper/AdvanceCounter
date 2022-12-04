package com.commanderpepper.advancecounter.data.module

import com.commanderpepper.advancecounter.data.repository.CounterRepository
import com.commanderpepper.advancecounter.data.repository.CounterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CounterRepoModule {

    @Binds
    fun bindsCounterRepository(
        counterRepositoryImpl: CounterRepositoryImpl
    ): CounterRepository
}