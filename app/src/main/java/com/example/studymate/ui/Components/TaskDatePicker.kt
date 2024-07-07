package com.example.studymate.ui.Components

import android.widget.AutoCompleteTextView.OnDismissListener
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun taskDatePicker(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismissListener: () -> Unit,
    onConfirmListener: () -> Unit,
    state: DatePickerState
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissListener,
            confirmButton = {
                TextButton(onClick = onConfirmListener) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissListener) {
                    Text(text = "Cancel")
                }
            },
            content = {
                DatePicker(
                    state = state,
                    dateValidator = { timestamp ->
                        val selectedDate =
                            Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        val currentDate = LocalDate.now(ZoneId.systemDefault())
                        selectedDate >= currentDate
                    }
                )
            }
        )
    }

}