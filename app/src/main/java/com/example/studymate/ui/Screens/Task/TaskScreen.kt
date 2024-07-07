package com.example.studymate.ui.Screens.Task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studymate.ui.Components.DeleteDialog
import com.example.studymate.ui.Components.SubjectListBottomSheet
import com.example.studymate.ui.Components.TaskCheckBox
import com.example.studymate.ui.Components.taskDatePicker
import com.example.studymate.ui.Util.Priority
import com.example.studymate.ui.Util.SnackbarEvent
import com.example.studymate.ui.Util.changeMillisToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class TaskScreenNavArgs(
    val taskId: Int?,
    val subjectId: Int?
)

@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreenRoute(modifier: Modifier = Modifier, navigator: DestinationsNavigator) {

    val viewModel: TaskScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    TaskScreen(
        state = state,
        snackbarEvent = viewModel.snackbarEventFlow,
        onEvent = viewModel::onEvent,
        onBackButtonClicked = { navigator.navigateUp() })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreen(
    modifier: Modifier = Modifier,
    state: TaskState,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    onEvent: (TaskEvent) -> Unit,
    onBackButtonClicked: () -> Unit
) {

    val scope = rememberCoroutineScope()

    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isTaskDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var isBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    var taskDescriptionError by rememberSaveable { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackbarEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {
                    onBackButtonClicked()
                }
            }
        }
    }

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissListener = { isDeleteSubjectDialogOpen = false },
        onConfirmListener = {
            onEvent(TaskEvent.DeleteTask)
            isDeleteSubjectDialogOpen = false
        }
    )

    taskTitleError = when {
        state.title.isBlank() -> "Please task title."
        state.title.length < 2 -> "Subject name is too short."
        state.title.length > 20 -> "Subject name is too long."
        else -> null
    }
    taskDescriptionError = when {
        state.description.isBlank() -> "Please enter description."
        state.description.length < 5 -> "Description is too short."
        else -> null
    }

    taskDatePicker(
        isOpen = isTaskDatePicker,
        onDismissListener = { isTaskDatePicker = false },
        onConfirmListener = {
            onEvent(TaskEvent.OnDateChange(millis = datePickerState.selectedDateMillis))
            isTaskDatePicker = false
        },
        state = datePickerState,
    )

    SubjectListBottomSheet(
        sheetState = bottomSheetState,
        isOpen = isBottomSheet,
        subjects = state.subjects,
        onDismissRequest = { isBottomSheet = false },
        onSubjectClicked = { subject ->
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) isBottomSheet = false
            }
            onEvent(TaskEvent.OnRelatedSubjectSelect(subject))
        })

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TaskScreenTopAppBar(
                isTaskExist = state.currentTaskId != null,
                isComplete = state.isTaskComplete,
                checkBoxBorderColor = state.priority.color,
                onBackClick = onBackButtonClicked,
                onDeleteClick = { isDeleteSubjectDialogOpen = true },
                onCheckBoxClick = { onEvent(TaskEvent.OnIsCompleteChange) }
            )
        }
    )
    { paddingValues ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    value = state.title,
                    onValueChange = { onEvent(TaskEvent.OnTitleChange(it)) },
                    label = { Text(text = "Task") },
                    singleLine = true,
                    isError = taskTitleError != null && state.title.isNotBlank(),
                    supportingText = { Text(text = taskTitleError.orEmpty()) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    value = state.description,
                    onValueChange = { onEvent(TaskEvent.OnDescriptionChange(it)) },
                    label = { Text(text = "Description") },
                    isError = taskDescriptionError != null && state.description.isNotBlank(),
                    supportingText = { Text(text = taskDescriptionError.orEmpty()) },
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Due date",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = {
                        isTaskDatePicker = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Delete Button"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Priority.entries.forEach { priority ->
                        PriorityButton(
                            modifier = Modifier.weight(1f),
                            label = priority.title,
                            backgroundColor = priority.color,
                            borderColor = if (priority == state.priority) {
                                Color.White
                            } else Color.Transparent,
                            labelColor = if (priority == state.priority) {
                                Color.White
                            } else Color.White.copy(alpha = 0.7f),
                            onClick = { onEvent(TaskEvent.OnPriorityChange(priority)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Related to subject",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val firstSubject = state.subjects.firstOrNull()?.name ?: ""
                    Text(
                        text = state.relatedToSubject ?: firstSubject,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = { isBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Subject"
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    enabled = taskTitleError == null,
                    onClick = { onEvent(TaskEvent.SaveTask) }) {
                    Text(text = "Save")
                }


            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenTopAppBar(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit,
    isTaskExist: Boolean,
    isComplete: Boolean,
    checkBoxBorderColor: androidx.compose.ui.graphics.Color,
    onCheckBoxClick: () -> Unit
) {
    androidx.compose.material3.TopAppBar(
        title = {
            Text(
                text = "Task",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
            }
        },
        actions = {
            if (isTaskExist) {
                TaskCheckBox(
                    isComplete = isComplete,
                    boarderColor = checkBoxBorderColor,
                    onBoxClick = onCheckBoxClick
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Button")
                }
            }

        }
    )
}

@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = labelColor)
    }
}
