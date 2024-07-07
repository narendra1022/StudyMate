package com.example.studymate.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studymate.ui.Models.Subject

@Composable
fun AlertDialogBoxForAddingSubjects(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    title: String = "Add/Update Subject",
    subjectName: String,
    goalHours: String,
    onDismissListener: () -> Unit,
    onConfirmListener: () -> Unit,
    onSubjectNameChange: (String) -> Unit,
    onGoalHoursChange: (String) -> Unit,
    selectedColor: List<Color>,
    onChangeColor: (List<Color>) -> Unit
) {

    var subjectNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var goalHoursError by rememberSaveable { mutableStateOf<String?>(null) }

    subjectNameError = when {
        subjectName.isBlank() -> "Please enter subject name."
        subjectName.length < 2 -> "Subject name is too short."
        subjectName.length > 20 -> "Subject name is too long."
        else -> null
    }
    goalHoursError = when {
        goalHours.isBlank() -> "Please enter goal study hours."
        goalHours.toFloatOrNull() == null -> "Invalid number."
        goalHours.toFloat() < 1f -> "Please set at least 1 hour."
        goalHours.toFloat() > 1000f -> "Please set a maximum of 1000 hours."
        else -> null
    }

    if (isOpen) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onDismissListener,
            confirmButton = {
                TextButton(
                    onClick = onConfirmListener,
                    enabled = subjectNameError == null && goalHoursError == null
                ) {
                    Text(text = "Confirm")
                }
            },
            text = {

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Subject.subjectCardColors.forEach { colors ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        shape = CircleShape,
                                        color = if (colors == selectedColor) Color.Black else Color.Transparent
                                    )
                                    .background(brush = Brush.horizontalGradient(colors))
                                    .clickable { onChangeColor(colors) }
                            ) {

                            }
                        }
                    }

                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = onSubjectNameChange,
                        label = { Text(text = "Subject Name") },
                        singleLine = true,
                        isError = subjectNameError != null && subjectName.isNotBlank(),
                        supportingText = { Text(text = subjectNameError.orEmpty()) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = goalHours,
                        onValueChange = onGoalHoursChange,
                        label = { Text(text = "Goal Study Hours") },
                        singleLine = true,
                        isError = goalHoursError != null && goalHours.isNotBlank(),
                        supportingText = { Text(text = goalHoursError.orEmpty()) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )


                }

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
