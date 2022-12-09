package com.commanderpepper.advancecounter.ui.items

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource

@Composable
fun CounterItemOptionsButton(
    counterOptionImageResource: Int,
    showDeleteOption: Boolean = true,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    val isDropdownMenuVisible = remember { mutableStateOf(false) }
    IconButton(onClick = {
        isDropdownMenuVisible.value = true
    }) {
        val painter = painterResource(id = counterOptionImageResource)
        Icon(painter = painter, contentDescription = "Add new counter")
        DropdownMenu(expanded = isDropdownMenuVisible.value, onDismissRequest = {
            isDropdownMenuVisible.value = false
        }) {
            DropdownMenuItem(
                text = { Text(text = "Edit Counter") },
                onClick = {
                    onEditClicked()
                    isDropdownMenuVisible.value = false
                }
            )
            if (showDeleteOption) {
                DropdownMenuItem(
                    text = { Text(text = "Delete Counter") },
                    onClick = {
                        onDeleteClicked()
                        isDropdownMenuVisible.value = false
                    }
                )
            }
        }
    }
}