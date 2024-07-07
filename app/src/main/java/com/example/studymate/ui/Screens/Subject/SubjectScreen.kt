package com.example.studymate.ui.Screens.Subject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studymate.ui.Components.AlertDialogBoxForAddingSubjects
import com.example.studymate.ui.Components.DeleteDialog
import com.example.studymate.ui.Components.studySessionsList
import com.example.studymate.ui.Components.tasksList
import com.example.studymate.ui.Screens.Task.TaskScreenNavArgs
import com.example.studymate.ui.Screens.destinations.TaskScreenRouteDestination
import com.example.studymate.ui.Util.SnackbarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class SubjectScreenNavArgs(
    val subjectId: Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator
) {

    val viewModel: SubjectViewModel = hiltViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    SubjectScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarEvent = viewModel.snackbarEventFlow,
        onBackButtonClick = { navigator.navigateUp() },
        onTaskClicked = { taskId ->
            val navArgs = TaskScreenNavArgs(
                taskId = taskId,
                subjectId = null
            )
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        },
        onNewTaskButtonClicked = {
            val navArgs = TaskScreenNavArgs(
                taskId = null,
                subjectId = state.currentSubjectId
            )
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    modifier: Modifier = Modifier,
    state: SubjectStates,
    onEvent: (SubjectEvent) -> Unit,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    onBackButtonClick: () -> Unit,
    onTaskClicked: (Int?) -> Unit,
    onNewTaskButtonClicked: () -> Unit
) {


    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }


    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackbarEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {}
            }
        }
    }

    LaunchedEffect(key1 = state.studiedHours, key2 = state.goalStudyHours) {
        onEvent(SubjectEvent.UpdateProgress)
    }


    AlertDialogBoxForAddingSubjects(
        isOpen = isEditSubjectDialogOpen,
        onDismissListener = { isEditSubjectDialogOpen = false },
        onConfirmListener = {
            isEditSubjectDialogOpen = false
        },
        onSubjectNameChange = { onEvent(SubjectEvent.OnSubjectNameChange(it)) },
        onGoalHoursChange = { onEvent(SubjectEvent.OnGoalStudyHoursChange(it)) },
        goalHours = state.goalStudyHours,
        subjectName = state.subjectName,
        selectedColor = state.subjectCardColors,
        onChangeColor = { onEvent(SubjectEvent.OnSubjectCardColorChange(it)) }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissListener = { isDeleteSubjectDialogOpen = false },
        onConfirmListener = {
            onEvent(SubjectEvent.DeleteSubject)
            isDeleteSubjectDialogOpen = false

        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissListener = { isDeleteSessionDialogOpen = false },
        onConfirmListener = {
            onEvent(SubjectEvent.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = state.subjectName,
                onDeleteClick = { isDeleteSubjectDialogOpen = true },
                onBackClick = onBackButtonClick,
                onEditClick = { isEditSubjectDialogOpen = true },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onNewTaskButtonClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Floating Action Button"
                )
                Text(text = "Add Task")
            }
        }
    )
    { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                CountCardInSubjectScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = state.studiedHours.toString(),
                    goalHours = state.goalStudyHours,
                    progress = state.progress
                )
            }

            tasksList(
                sectionTitle = "UPCOMING TASKS",
                tasks = state.upcomingTasks,
                emptyTaskText = "You don't have any Tasks . \nPlease add by clicking + on top right",
                onCheckBoxClick = { onEvent(SubjectEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskClicked
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            tasksList(
                sectionTitle = "COMPLETED TASKS",
                emptyTaskText = "You don't have any Tasks . \n Click the check box on completion of Task",
                tasks = state.completedTasks,
                onCheckBoxClick = { onEvent(SubjectEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskClicked
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionsList(
                "RECENT STUDY SESSIONS",
                sessions = state.recentSessions,
                emptySessionText = "You don't have any Session . \nYou have completed all tasks",
                onDeleteIcon = {
                    onEvent(SubjectEvent.OnDeleteSessionButtonClick(it))
                    isDeleteSessionDialogOpen = true
                }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Buttton"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Button")
            }
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "Edit Button")
            }
        }
    )
}

@Composable
fun CountCardInSubjectScreen(
    modifier: Modifier = Modifier,
    studiedHours: String,
    goalHours: String,
    progress: Float

) {

    val progressPercentage = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    Row(modifier = modifier.padding(horizontal = 3.dp)) {
        com.example.studymate.ui.Components.CountCard(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
            headingText = "Studied Hours",
            count = studiedHours
        )
        com.example.studymate.ui.Components.CountCard(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
            headingText = "Goal Hours",
            count = goalHours
        )
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        )
        {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round,
                strokeWidth = 4.dp
            )
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
                strokeCap = StrokeCap.Round,
                strokeWidth = 4.dp
            )
            Text(text = progressPercentage.toString())
        }
    }
}