package com.commanderpepper.advancecounter.usecase

import com.commanderpepper.advancecounter.model.database.Counter
import com.commanderpepper.advancecounter.model.repo.CounterRepo
import javax.inject.Inject

class ConvertCounterRepoToCounterUseCase @Inject constructor() {
    operator fun invoke(counterRepo: CounterRepo): Counter {
        return Counter(
            id = counterRepo.id,
            name = counterRepo.name,
            value = counterRepo.value,
            step = counterRepo.step,
            threshold = counterRepo.threshold,
            upperThreshold = counterRepo.upperThreshold,
            lowerThreshold = counterRepo.lowerThreshold,
            parentId = counterRepo.parentId,
            relationship = counterRepo.relationship
        )
    }
}