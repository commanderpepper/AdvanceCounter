package com.commanderpepper.advancecounter.ui.parentcounters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.commanderpepper.advancecounter.data.model.CounterRepo
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ParentCounters(
    modifier: Modifier = Modifier,
    parentCountersViewModel: ParentCountersViewModel = hiltViewModel()
) {
    ParentCounters(modifier = modifier, parentCounters = parentCountersViewModel.parentCounters)
}

@Composable
fun ParentCounters(modifier: Modifier = Modifier, parentCounters: StateFlow<List<CounterRepo>>) {
    val parentCountersState = parentCounters.collectAsState()
    LazyColumn {
        items(items = parentCountersState.value, itemContent = { item ->
            Row() {
                Text(text = item.name)
            }
        })
    }
}