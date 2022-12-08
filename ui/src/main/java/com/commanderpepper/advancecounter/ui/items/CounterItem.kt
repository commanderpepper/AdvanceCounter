package com.commanderpepper.advancecounter.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.commanderpepper.advancecounter.model.ui.CounterItemUIState

@Composable
fun CounterItem(
    modifier: Modifier = Modifier,
    counterItemUIState: CounterItemUIState,
    counterClicked: (Long) -> Unit,
    onMinusClicked: (Long) -> Unit,
    onPlusClicked: (Long) -> Unit,
) {
    Card(modifier = modifier.padding(8.dp)) {
        Column() {
            Column(modifier = Modifier.clickable { counterClicked(counterItemUIState.id) }) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Name: ${counterItemUIState.name}, ID: ${counterItemUIState.id}",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Value: ${counterItemUIState.value}",
                    style = MaterialTheme.typography.labelLarge
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

@Preview
@Composable
fun CounterItemPreview() {
    CounterItem(
        counterItemUIState = CounterItemUIState(1L, "Test", "10", "1","-7", "7"),
        counterClicked = {},
        onMinusClicked = {},
        onPlusClicked = {})
}