package com.commanderpepper.advancecounter.ui.editcounterdialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.commanderpepper.advancecounter.model.ui.editcounter.EditCounterState
import com.commanderpepper.advancecounter.model.ui.editcounter.ExistingCounterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCounterDialog(
    modifier: Modifier = Modifier,
    existingCounterState: ExistingCounterState,
    onDismissRequest: () -> Unit,
    onConfirmClick: (EditCounterState) -> Unit){

    val counterName = remember {
        mutableStateOf(existingCounterState.counterName)
    }
    val counterStep = remember {
        mutableStateOf(existingCounterState.counterStep)
    }
    val counterValue = remember {
        mutableStateOf(existingCounterState.counterValue)
    }
    val counterThreshold = remember {
        mutableStateOf(existingCounterState.counterThreshold)
    }
    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Edit Counter")
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "No, thanks")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        counterStep.value.isDigitsOnly().not() || counterStep.value.isBlank() || counterStep.value.toLong() <= 0 -> {
                            Toast.makeText(context, "Step cannot be less than zero", Toast.LENGTH_SHORT).show()
                        }
                        counterStep.value.isDigitsOnly().not() || counterStep.value.isBlank() || counterStep.value.toLong() <= 0 -> {
                            Toast.makeText(context, "Step cannot be less than zero", Toast.LENGTH_SHORT).show()
                        }
                        counterThreshold.value.isDigitsOnly().not() || counterThreshold.value.isBlank() || counterThreshold.value.toLong() <= 0 -> {
                            Toast.makeText(context, "Threshold cannot be less than zero", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            onConfirmClick(
                                EditCounterState(
                                    counterId = existingCounterState.counterId,
                                    counterName = counterName.value,
                                    counterStep = counterStep.value.toLong(),
                                    counterThreshold = counterThreshold.value.toLong(),
                                    counterValue = counterValue.value.toLong()
                                )
                            )
                        }
                    }

                }
            ){
                Text(text = "Save Changes")
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        Text(text = counterValue.value, style = MaterialTheme.typography.bodyMedium)
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
                    onValueChange = { counterStep.value = it },
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    value = counterThreshold.value,
                    label = {
                        Text(text = "Threshold", style = MaterialTheme.typography.labelSmall)
                    },
                    placeholder = {
                        Text(text = counterThreshold.value, style = MaterialTheme.typography.bodyMedium)
                    },
                    onValueChange = { counterThreshold.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        },
        onDismissRequest = onDismissRequest
    )
}