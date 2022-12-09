package com.commanderpepper.advancecounter.ui.editcounterdialog

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = "Edit a counter")
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "No, thanks")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmClick(
                        EditCounterState(
                            counterId = existingCounterState.counterId,
                            counterName = counterName.value
                        )
                    )
                }
            ){
                Text(text = "Save Changes")
            }
        },
        text = {
            TextField(
                value = counterName.value,
                label = {
                    Text(text = "Name", style = MaterialTheme.typography.labelSmall)
                },
                onValueChange = { counterName.value = it },
                textStyle = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = onDismissRequest
    )
}