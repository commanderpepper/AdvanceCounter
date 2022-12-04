package com.commanderpepper.advancecounter.ui.items

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class CounterItemUIState(
    val id: Long,
    val name: String,
    val value: String
)

@Composable
fun CounterItem(
    modifier: Modifier = Modifier,
    counterItemUIState: CounterItemUIState,
    onMinusClicked: (Long) -> Unit,
    onPlusClicked: (Long) -> Unit,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Name: ${counterItemUIState.name}, ID: ${counterItemUIState.id}")
        Text(text = "Value: ${counterItemUIState.value}")
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(modifier = Modifier.weight(1f), onClick = { onMinusClicked(counterItemUIState.id) }) {
                Text(text = "-")
            }
            Button(modifier = Modifier.weight(1f), onClick = { onPlusClicked(counterItemUIState.id) }) {
                Text(text = "+")
            }
        }
    }
}

@Preview
@Composable
fun CounterItemPreview(){
    CounterItem(counterItemUIState = CounterItemUIState(1L, "Test", "10"), onMinusClicked = {}, onPlusClicked = {})
}