package com.commanderpepper.advancecounter.ui.deletecounterdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeleteCounterDialog(
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
){
    AlertDialog(
        modifier = modifier,
        title = {
            Text("Delete Counter", style = MaterialTheme.typography.titleMedium)
        },
        text = {
               Text(text = "Do you want to delete the counter?", style = MaterialTheme.typography.bodyMedium)
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

@Preview
@Composable
fun DeleteCounterDialogPreview(){
    DeleteCounterDialog(onConfirmClick = { }) {
        
    }
}