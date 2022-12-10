package com.commanderpepper.advancecounter.usecase

import com.commanderpepper.advancecounter.model.repo.CounterRepo
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState
import javax.inject.Inject

class ConvertCounterRepoToCounterItemUIStateUseCase @Inject constructor() {
    operator fun invoke(counterRepo: CounterRepo): CounterItemUIState {
        return CounterItemUIState(
            id = counterRepo.id,
            name = counterRepo.name,
            value = counterRepo.value.toString(),
            step = counterRepo.step.toString(),
            upperThreshold = counterRepo.upperThreshold.toString(),
            lowerThreshold = counterRepo.lowerThreshold.toString(),
            threshold = counterRepo.threshold.toString()
        )
    }
}