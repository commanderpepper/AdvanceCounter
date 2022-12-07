package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState
import com.commanderpepper.advancecounter.ui.addcounterdialog.AddCounterDialog
import com.commanderpepper.advancecounter.ui.items.CounterItem
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCountersViewModel: ParentCountersViewModel = hiltViewModel(),
    addCounterImageResource: Int,
    topAppBarTitle: String,
    counterOnClick: (Long) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = topAppBarTitle)
            },
            actions = {
                IconButton(onClick = { openDialog.value = true }) {
                    val painter = painterResource(id = addCounterImageResource)
                    Icon(painter = painter, contentDescription = "Add new counter")
                }
            })
        ParentCounters(
            modifier = modifier,
            parentCounters = parentCountersViewModel.parentCounters,
            counterOnClick = counterOnClick,
            onPlusClicked = parentCountersViewModel::plusButtonOnClick,
            onMinusClicked = parentCountersViewModel::minusButtonOnClick
        )
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
    counterOnClick: (Long) -> Unit,
    onPlusClicked: (Long) -> Unit,
    onMinusClicked: (Long) -> Unit
) {
    val parentCountersState = parentCounters.collectAsState()
    LazyColumn(modifier = modifier) {
        items(items = parentCountersState.value, itemContent = { item ->
            CounterItem(
                counterItemUIState = item,
                counterClicked = counterOnClick,
                onMinusClicked = onMinusClicked,
                onPlusClicked = onPlusClicked
            )
        })
    }
}