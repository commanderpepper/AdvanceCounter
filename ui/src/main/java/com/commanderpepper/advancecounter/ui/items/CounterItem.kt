package com.commanderpepper.advancecounter.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState

@Composable
fun CounterItem(
    modifier: Modifier = Modifier,
    counterItemUIState: CounterItemUIState,
    optionsImageResource: Int,
    counterClicked: (Long) -> Unit,
    onMinusClicked: (Long) -> Unit,
    onPlusClicked: (Long) -> Unit,
    onEditClicked: (Long) -> Unit,
    onDeleteClicked: (Long) -> Unit
) {
    Card(modifier = modifier.padding(8.dp)) {
        Column() {
            Column(modifier = Modifier.clickable { counterClicked(counterItemUIState.id) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(9 / 10f),
                        text = "Name: ${counterItemUIState.name}, ID: ${counterItemUIState.id}",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2
                    )
                    CounterOptionsButton(
                        counterOptionImageResource = optionsImageResource,
                        counterId = counterItemUIState.id,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked
                    )
                }
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Value: ${counterItemUIState.value}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row() {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Lower Threshold: ${counterItemUIState.lowerThreshold}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Upper Threshold: ${counterItemUIState.upperThreshold}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onMinusClicked(counterItemUIState.id) }) {
                    Text(text = "- ${counterItemUIState.step}")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onPlusClicked(counterItemUIState.id) }) {
                    Text(text = "+ ${counterItemUIState.step}")
                }
            }
        }
    }
}

@Composable
fun CounterOptionsButton(counterOptionImageResource: Int,
                         counterId: Long,
                         onEditClicked: (Long) -> Unit,
                         onDeleteClicked: (Long) -> Unit) {
    val isDropdownMenuVisible = remember { mutableStateOf(false) }
    IconButton(onClick = {
        isDropdownMenuVisible.value = true
    }) {
        val painter = painterResource(id = counterOptionImageResource)
        Icon(painter = painter, contentDescription = "Add new counter")
        DropdownMenu(expanded = isDropdownMenuVisible.value, onDismissRequest = {
            isDropdownMenuVisible.value = false
        }) {
            DropdownMenuItem(
                text = { Text(text = "Edit Counter") },
                onClick = {
                    // TODO: Replace with method from view model
                    onEditClicked(counterId)
//                    isDropdownMenuVisible.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Delete Counter") },
                onClick = {
                    // TODO: Replace with method from view model
                    onDeleteClicked(counterId)
//                    isDropdownMenuVisible.value = false
                }
            )
        }
    }
}

@Preview
@Composable
fun CounterItemPreview() {
    CounterItem(
        counterItemUIState = CounterItemUIState(1L, "Test", "10", "1","-7", "7"),
        counterClicked = {},
        onMinusClicked = {},
        onPlusClicked = {},
        onDeleteClicked = {},
        onEditClicked = {},
        optionsImageResource = 1
    )
}