package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.commanderpepper.advancecounter.model.ui.CounterListUIState
import com.commanderpepper.advancecounter.model.ui.editcounter.EditCounterState
import com.commanderpepper.advancecounter.ui.addcounterdialog.AddCounterDialog
import com.commanderpepper.advancecounter.ui.generic.LoadingIndicator
import com.commanderpepper.advancecounter.ui.items.CounterItem
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCountersViewModel: ParentCountersViewModel = hiltViewModel(),
    addCounterImageResource: Int,
    topAppBarTitle: String,
    counterOptionImageResource: Int,
    counterOnClick: (Long) -> Unit,
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
            parentCounterListUI = parentCountersViewModel.parentCounterListUIState,
            counterOnClick = counterOnClick,
            onPlusClicked = parentCountersViewModel::plusButtonOnClick,
            onMinusClicked = parentCountersViewModel::minusButtonOnClick,
            counterOptionImageResource = counterOptionImageResource,
            onDeleteClicked = parentCountersViewModel::deleteCounter,
            onEditClicked = parentCountersViewModel::editCounter
        )
        if (openDialog.value) {
            AddCounterDialog(
                onDismissRequest = { openDialog.value = false },
                onConfirmClick = {
                    parentCountersViewModel.addNewParentCounter(it)
                    openDialog.value = false
                },
                allowForUserToDefineRelationship = true)
        }
    }
}

@Composable
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCounterListUI: StateFlow<CounterListUIState>,
    counterOnClick: (Long) -> Unit,
    onPlusClicked: (Long) -> Unit,
    onMinusClicked: (Long) -> Unit,
    onDeleteClicked: (Long) -> Unit,
    onEditClicked: (EditCounterState) -> Unit,
    counterOptionImageResource: Int
) {
    val parentCounterListUIState = parentCounterListUI.collectAsState()
    when (parentCounterListUIState.value) {
        is CounterListUIState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = (parentCounterListUIState.value as CounterListUIState.Error).message)
            }
        }
        is CounterListUIState.Success -> {
            val success = parentCounterListUIState.value as? CounterListUIState.Success
            success?.let { state ->
                LazyColumn(modifier = modifier) {
                    items(items = state.list, itemContent = { item ->
                        CounterItem(
                            counterItemUIState = item,
                            counterClicked = counterOnClick,
                            onMinusClicked = onMinusClicked,
                            onPlusClicked = onPlusClicked,
                            optionsImageResource = counterOptionImageResource,
                            onEditClicked = onEditClicked,
                            onDeleteClicked = onDeleteClicked
                        )
                    })
                }
            }
        }
        is CounterListUIState.Loading -> {
            LoadingIndicator()
        }
    }

}