package com.example.studymate.ui.Components


import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun DeleteDialog(
    isOpen: Boolean,
    title: String = "Delete Subject",
    body: String,
    onDismissListener: () -> Unit,
    onConfirmListener: () -> Unit
) {
    if (isOpen) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismissListener,
            confirmButton = {
                TextButton(
                    onClick = onConfirmListener,
                ) {
                    Text(text = "Delete")
                }
            },
            text = {
                Text(text = body)
            },
            title = {
                Text(text = title)
            },
            dismissButton = {
                TextButton(onClick = onDismissListener) {
                    Text(text = "Cancel")
                }
            },
            tonalElevation = 10.dp
        )
    }


}
