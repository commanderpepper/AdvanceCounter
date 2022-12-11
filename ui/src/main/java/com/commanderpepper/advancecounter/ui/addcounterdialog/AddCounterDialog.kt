package com.commanderpepper.advancecounter.ui.addcounterdialog

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.commanderpepper.advancecounter.model.ui.AddCounterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCounterDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmClick: (AddCounterState) -> Unit,
    allowForUserToDefineRelationship: Boolean = false,
    parentRelationship: Long = 1
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
    val counterRelationship = remember {
        mutableStateOf(parentRelationship)
    }

    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Add a counter", style = MaterialTheme.typography.titleMedium)
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
                        Text(text = "Starting Value", style = MaterialTheme.typography.labelSmall)
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
                if(allowForUserToDefineRelationship){
                    Column() {
                        // Parent to child
                        Row() {
                            Checkbox(checked = counterRelationship.value == 1L, onCheckedChange = {
                                counterRelationship.value = 1L
                            })
                            Text(text = "Parent counter effects child counters", style = MaterialTheme.typography.bodyMedium)
                        }
                        // Child to parent
                        Row() {
                            Checkbox(checked = counterRelationship.value == 2L, onCheckedChange = {
                                counterRelationship.value = 2L
                            })
                            Text(text = "Child counters effect parent counter", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "No, thanks")
            }
        },
        confirmButton = {
            Button(onClick = {
                when {
                    counterValue.value.isDigitsOnly().not() -> {
                        Toast.makeText(context, "Value must be a number", Toast.LENGTH_SHORT).show()
                    }
                    counterStep.value.isDigitsOnly().not() || counterStep.value.isBlank() || counterStep.value.toLong() <= 0 -> {
                        Toast.makeText(context, "Step cannot be less than zero", Toast.LENGTH_SHORT).show()
                    }
                    counterThreshold.value.isDigitsOnly().not() || counterThreshold.value.isBlank() || counterThreshold.value.toLong() <= 0 -> {
                        Toast.makeText(context, "Threshold cannot be less than zero", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        onConfirmClick(
                            AddCounterState(
                                name = counterName.value,
                                value = if(counterValue.value.isEmpty()) 0 else counterValue.value.toLong(),
                                step =  counterStep.value.toLong(),
                                threshold = counterThreshold.value.toLong(),
                                relationship = counterRelationship.value
                            )
                        )
                    }
                }
            }) {
                Text(text = "Yeah, thanks")
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@Preview
@Composable
fun AddCounterDialogPreview(){
    AddCounterDialog(onDismissRequest = { }, onConfirmClick = { } )
}

@Preview
@Composable
fun AddCounterDialogPreviewRelationshipVisible(){
    AddCounterDialog(onDismissRequest = { }, onConfirmClick = { }, allowForUserToDefineRelationship = true )
}