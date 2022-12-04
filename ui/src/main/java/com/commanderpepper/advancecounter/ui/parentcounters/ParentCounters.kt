package com.commanderpepper.advancecounter.ui.parentcounters

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
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCountersViewModel: ParentCountersViewModel = hiltViewModel()
) {
    ParentCounters(
        modifier = modifier,
        parentCounters = parentCountersViewModel.parentCounters,
        onPlusClicked = parentCountersViewModel::plusButtonOnClick,
        onMinusClicked = parentCountersViewModel::minusButtonOnClick
    )
}

@Composable
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCounters: StateFlow<List<CounterItemUIState>>,
    onPlusClicked: (Long) -> Unit,
    onMinusClicked: (Long) -> Unit
) {
    val parentCountersState = parentCounters.collectAsState()
    LazyColumn(modifier = modifier) {
        items(items = parentCountersState.value, itemContent = { item ->
            CounterItem(
                counterItemUIState = item,
                onMinusClicked = onMinusClicked,
                onPlusClicked = onPlusClicked
            )
        })
    }
}