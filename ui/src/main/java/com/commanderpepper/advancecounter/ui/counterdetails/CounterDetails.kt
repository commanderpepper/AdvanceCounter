package com.commanderpepper.advancecounter.ui.counterdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.commanderpepper.advancecounter.ui.AddCounterState
import com.commanderpepper.advancecounter.ui.items.CounterItem
import com.commanderpepper.advancecounter.ui.items.CounterItemUIState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CounterDetails(modifier: Modifier = Modifier, counterDetailsViewModel: CounterDetailsViewModel = hiltViewModel(), counterOnClick: (Long) -> Unit) {
    CounterDetails(modifier = modifier,
        parentCounterItemUI = counterDetailsViewModel.parentCounter,
        childCounters = counterDetailsViewModel.childCounters,
        addCounterOnClick = counterDetailsViewModel::addCounter,
        counterOnClick = counterOnClick)
}

@Composable
fun CounterDetails(modifier: Modifier = Modifier,
                   parentCounterItemUI: StateFlow<CounterItemUIState>,
                   childCounters: StateFlow<List<CounterItemUIState>>,
                   addCounterOnClick: (AddCounterState) -> Unit,
                   counterOnClick: (Long) -> Unit
){
    val parentCounterItemUIState = parentCounterItemUI.collectAsState()
    val childCountersState = childCounters.collectAsState()
    Box(){
        val openDialog = remember { mutableStateOf(false) }
        Column() {
            CounterItem(counterItemUIState = parentCounterItemUIState.value, onMinusClicked = {}, onPlusClicked = {}, counterClicked = {})
            LazyColumn(modifier = modifier) {
                items(items = childCountersState.value, itemContent = { item ->
                    CounterItem(
                        counterItemUIState = item,
                        onMinusClicked = {},
                        onPlusClicked = {},
                        counterClicked = {
                            counterOnClick(it)
                        }
                    )
                })
            }
        }
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
                    addCounterOnClick(it)
                    openDialog.value = false
                })
        }
    }

}