package com.commanderpepper.advancecounter.usecase

import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import javax.inject.Inject

class ConvertCounterRepoToCounterItemUIStateUseCase @Inject constructor() {
    operator fun invoke(counterRepo: CounterRepo): CounterItemUIState {
        return CounterItemUIState(
            id = counterRepo.id,
            name = counterRepo.name,
            value = counterRepo.value.toString(),
            upperThreshold = counterRepo.upperThreshold.toString(),
            lowerThreshold = counterRepo.lowerThreshold.toString()
        )
    }
}