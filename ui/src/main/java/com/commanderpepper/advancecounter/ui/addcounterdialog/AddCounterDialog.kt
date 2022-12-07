package com.commanderpepper.advancecounter.ui.addcounterdialog

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
import com.commanderpepper.advancecounter.model.ui.AddCounterState

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
    val counterStep = remember {
        mutableStateOf("")
    }
    val counterThreshold = remember {
        mutableStateOf("")
    }

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Add a counter")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                TextField(
                    value = counterStep.value,
                    label = {
                        Text(text = "Step", style = MaterialTheme.typography.labelSmall)
                    },
                    placeholder = {
                        Text(text = "1", style = MaterialTheme.typography.bodyMedium)
                    },
                    onValueChange = { counterStep.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    value = counterThreshold.value,
                    label = {
                        Text(text = "Threshold", style = MaterialTheme.typography.labelSmall)
                    },
                    placeholder = {
                        Text(text = "1", style = MaterialTheme.typography.bodyMedium)
                    },
                    onValueChange = { counterThreshold.value = it },
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
                        value = if(counterValue.value.isEmpty()) 0 else counterValue.value.toLong(),
                        step =  if(counterStep.value.isEmpty()) 1 else counterStep.value.toLong(),
                        threshold = if(counterThreshold.value.isEmpty()) 1 else counterThreshold.value.toLong()
                    )
                )
            }) {
                Text(text = "Yeah, thanks")
            }
        },
        onDismissRequest = onDismissRequest
    )
}