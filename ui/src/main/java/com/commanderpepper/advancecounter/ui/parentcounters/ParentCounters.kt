package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commanderpepper.advancecounter.ui.AddCounterDialog
import com.commanderpepper.advancecounter.ui.items.CounterItem
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCountersViewModel: ParentCountersViewModel = hiltViewModel()
) {

    Box(modifier = Modifier.fillMaxSize()) {
        val openDialog = remember { mutableStateOf(false) }
        ParentCounters(
            modifier = modifier,
            parentCounters = parentCountersViewModel.parentCounters,
            onPlusClicked = parentCountersViewModel::plusButtonOnClick,
            onMinusClicked = parentCountersViewModel::minusButtonOnClick
        )
        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { openDialog.value = true }) {
            Text(text = "Add new counter")
        }
        if (openDialog.value) {
            AddCounterDialog(
                onDismissRequest = { openDialog.value = false },
                onConfirmClick = {
                    parentCountersViewModel.addNewParentCounter(it)
                    openDialog.value = false
                })
        }
    }
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