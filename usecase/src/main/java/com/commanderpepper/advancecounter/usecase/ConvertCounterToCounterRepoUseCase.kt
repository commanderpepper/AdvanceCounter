package com.commanderpepper.advancecounter.usecase

import com.commanderpepper.advancecounter.model.database.Counter
import com.commanderpepper.advancecounter.model.repo.CounterRepo
import javax.inject.Inject

class ConvertCounterToCounterRepoUseCase @Inject constructor() {
    operator fun invoke(counter: Counter): CounterRepo {
        return CounterRepo(
            id = counter.id,
            name = counter.name,
            value = counter.value,
            step = counter.step,
            threshold = counter.threshold,
            upperThreshold = counter.upperThreshold,
            lowerThreshold = counter.lowerThreshold,
            parentId = counter.parentId
        )
    }
}