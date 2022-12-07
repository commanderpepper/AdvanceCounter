package com.commanderpepper.advancecounter.usecase

import com.commanderpepper.advancecounter.data.model.CounterRepo
import com.commanderpepper.advancecounter.ui.addcounterdialog.AddCounterState
import javax.inject.Inject

class ConvertAddCounterStateToCounterRepoUseCase @Inject constructor() {
    operator fun invoke(addCounterState: AddCounterState, parentId: Long?): CounterRepo {
        return CounterRepo(
            id = 0,
            name = addCounterState.name,
            value = addCounterState.value,
            step = addCounterState.step,
            threshold = addCounterState.threshold,
            upperThreshold = addCounterState.value + addCounterState.threshold,
            lowerThreshold = addCounterState.value - addCounterState.threshold,
            parentId = parentId
        )
    }
}