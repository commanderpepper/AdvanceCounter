package com.commanderpepper.advancecounter.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class CounterItemUIState(
    val id: Long,
    val name: String,
    val value: String
)

@Composable
fun CounterItem(
    modifier: Modifier = Modifier,
    counterItemUIState: CounterItemUIState,
    counterOnClick: (Long) -> Unit
) {
    Column(modifier = modifier.clickable { counterOnClick(counterItemUIState.id) }) {
        Text(text = "Name: ${counterItemUIState.name} (${counterItemUIState.id})")
        Text(text = "Value: ${counterItemUIState.value}")
    }
}