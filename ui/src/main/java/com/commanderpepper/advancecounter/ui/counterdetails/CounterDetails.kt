package com.commanderpepper.advancecounter.ui.counterdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.commanderpepper.advancecounter.model.ui.AddCounterState
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState
import com.commanderpepper.advancecounter.model.ui.editcounter.EditCounterState
import com.commanderpepper.advancecounter.ui.addcounterdialog.AddCounterDialog
import com.commanderpepper.advancecounter.ui.items.CounterItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CounterDetails(
    modifier: Modifier = Modifier,
    addCounterImageResource: Int,
    counterOptionImageResource: Int,
    counterDetailsViewModel: CounterDetailsViewModel = hiltViewModel(),
    counterOnClick: (Long) -> Unit
) {
    CounterDetails(
        modifier = modifier,
        parentCounterItemUI = counterDetailsViewModel.parentCounter,
        addCounterImageResource = addCounterImageResource,
        childCounters = counterDetailsViewModel.childCounters,
        addCounterOnClick = counterDetailsViewModel::addCounter,
        counterOnClick = counterOnClick,
        onPlusClicked = counterDetailsViewModel::plusButtonOnClick,
        onMinusClicked = counterDetailsViewModel::minusButtonOnClick,
        optionsImageResource = counterOptionImageResource,
        onEditClicked = counterDetailsViewModel::editCounter,
        onDeleteClicked = counterDetailsViewModel::deleteCounter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterDetails(
    modifier: Modifier = Modifier,
    addCounterImageResource: Int,
    parentCounterItemUI: StateFlow<CounterItemUIState>,
    childCounters: StateFlow<List<CounterItemUIState>>,
    optionsImageResource: Int,
    addCounterOnClick: (AddCounterState) -> Unit,
    counterOnClick: (Long) -> Unit,
    onPlusClicked: (Long) -> Unit,
    onMinusClicked: (Long) -> Unit,
    onDeleteClicked: (Long) -> Unit,
    onEditClicked: (EditCounterState) -> Unit
) {
    val parentCounterItemUIState = parentCounterItemUI.collectAsState()
    val childCountersState = childCounters.collectAsState()
    Column() {
        val openDialog = remember { mutableStateOf(false) }
        TopAppBar(
            title = {
                Text(text = parentCounterItemUIState.value.name, maxLines = 1)
            },
            actions = {
                IconButton(onClick = { openDialog.value = true }) {
                    val painter = painterResource(id = addCounterImageResource)
                    Icon(painter = painter, contentDescription = "Add new counter")
                }
            })
        CounterItem(
            counterItemUIState = parentCounterItemUIState.value,
            onMinusClicked = onMinusClicked
            ,
            onPlusClicked = onPlusClicked,
            counterClicked = {

            },
            optionsImageResource = optionsImageResource,
            onDeleteClicked = onDeleteClicked,
            onEditClicked = onEditClicked
        )
        LazyColumn(modifier = modifier) {
            items(items = childCountersState.value, itemContent = { item ->
                CounterItem(
                    counterItemUIState = item,
                    onMinusClicked = onMinusClicked,
                    onPlusClicked = onPlusClicked,
                    counterClicked = counterOnClick,
                    optionsImageResource = optionsImageResource,
                    onDeleteClicked = onDeleteClicked,
                    onEditClicked = onEditClicked
                )
            })
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