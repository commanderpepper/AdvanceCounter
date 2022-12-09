package com.commanderpepper.advancecounter.ui.deletecounterdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteCounterDialog(
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
){
    AlertDialog(
        modifier = modifier,
        title = {
            Text("Delete Counter")
        },
        text = {
               Text(text = "Do you want to delete the counter?")
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "Do Not Delete")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirmClick() }) {
                Text(text = "Delete Counter")
            }
        },
        onDismissRequest = onDismissRequest
    )
}