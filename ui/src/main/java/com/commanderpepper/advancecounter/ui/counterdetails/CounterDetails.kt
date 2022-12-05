package com.commanderpepper.advancecounter.ui.counterdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.commanderpepper.advancecounter.ui.items.CounterItem
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CounterDetails(modifier: Modifier = Modifier, counterDetailsViewModel: CounterDetailsViewModel = hiltViewModel()) {
    CounterDetails(modifier = modifier,
        parentCounterItemUI = counterDetailsViewModel.parentCounter,
        childCounters = counterDetailsViewModel.childCounters)
}

@Composable
fun CounterDetails(modifier: Modifier = Modifier,
                   parentCounterItemUI: StateFlow<CounterItemUIState>,
                   childCounters: StateFlow<List<CounterItemUIState>>
){
    val parentCounterItemUIState = parentCounterItemUI.collectAsState()
    val childCountersState = childCounters.collectAsState()
    Column() {
        CounterItem(counterItemUIState = parentCounterItemUIState.value, onMinusClicked = {}, onPlusClicked = {}, counterClicked = {})
        LazyColumn(modifier = modifier) {
            items(items = childCountersState.value, itemContent = { item ->
                CounterItem(
                    counterItemUIState = item,
                    onMinusClicked = {},
                    onPlusClicked = {},
                    counterClicked = {}
                )
            })
        }
    }
}