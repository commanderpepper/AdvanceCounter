package com.commanderpepper.advancecounter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCounterDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmClick: (AddCounterState) -> Unit
) {
    val counterName = remember {
        mutableStateOf("")
    }
    val counterValue = remember {
        mutableStateOf("")
    }

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Add a counter")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Add a name for the new counter",
                    style = MaterialTheme.typography.labelLarge
                )
                TextField(
                    value = counterName.value,
                    label = {
                        Text(text = "Name", style = MaterialTheme.typography.labelSmall)
                    },
                    onValueChange = { counterName.value = it },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    value = counterValue.value,
                    label = {
                        Text(text = "Value", style = MaterialTheme.typography.labelSmall)
                    },
                    placeholder = {
                        Text(text = "0", style = MaterialTheme.typography.bodyMedium)
                    },
                    onValueChange = { counterValue.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "No, thanks")
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirmClick(
                    AddCounterState(
                        name = counterName.value,
                        value = if(counterValue.value.isEmpty()) 0 else counterValue.value.toLong()
                    )
                )
            }) {
                Text(text = "Yeah, thanks")
            }
        },
        onDismissRequest = onDismissRequest
    )
}

data class AddCounterState(val name: String, val value: Long)