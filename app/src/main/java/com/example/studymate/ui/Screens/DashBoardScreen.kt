package com.example.studymate.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studymate.R
import com.example.studymate.ui.Components.AlertDialogBoxForAddingSubjects
import com.example.studymate.ui.Components.CountCard
import com.example.studymate.ui.Components.DeleteDialog
import com.example.studymate.ui.Components.SubjectCard
import com.example.studymate.ui.Components.studySessionsList
import com.example.studymate.ui.Components.tasksList
import com.example.studymate.ui.Events.DashboardEvent
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Subject
import com.example.studymate.ui.Models.Task
import com.example.studymate.ui.Screens.destinations.SessionScreenRouteDestination
import com.example.studymate.ui.Screens.destinations.SubjectScreenRouteDestination
import com.example.studymate.ui.Screens.destinations.TaskScreenRouteDestination
import com.example.studymate.ui.StateValues.DashBoardState
import com.example.studymate.ui.Util.SnackbarEvent
import com.example.studymate.ui.ViewModels.DashBoardViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination()
@Composable
fun DashBoardScreenRoute(
    navigator: DestinationsNavigator
) {

    val viewModel: DashBoardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    val tasks by viewModel.tasks.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    val recentSession by viewModel.recentSessions.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    DashBoardScreen(
        state = state,
        onEvent = viewModel::onEvent,
        tasks = tasks,
        snackbarEvent = viewModel.snackbarEventFlow,
        recentSessions = recentSession,
        onSubjectCardClicked = { subjectId ->
            subjectId?.let {
                val navArgs = SubjectScreenNavArgs(subjectId = subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs = navArgs))
            }

        }, onTaskCardClicked = { taskId ->
            val navArgs = TaskScreenNavArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArgs))
        },
        onSessionCardClicked = {
            navigator.navigate(SessionScreenRouteDestination)
        }
    )
}

@Composable
private fun DashBoardScreen(
    modifier: Modifier = Modifier,
    onEvent: (DashboardEvent) -> Unit,
    state: DashBoardState,
    tasks: List<Task>,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    recentSessions: List<Session>,
    onSubjectCardClicked: (Int?) -> Unit,
    onTaskCardClicked: (Int?) -> Unit,
    onSessionCardClicked: () -> Unit
) {


    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

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

    AlertDialogBoxForAddingSubjects(
        isOpen = isAddSubjectDialogOpen,
        onDismissListener = { isAddSubjectDialogOpen = false },
        onConfirmListener = {
            onEvent(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
        },
        onSubjectNameChange = { onEvent(DashboardEvent.OnSubjectNameChange(it)) },
        onGoalHoursChange = { onEvent(DashboardEvent.OnGoalStudyHoursChange(it)) },
        goalHours = state.goalStudyHours,
        subjectName = state.subjectName,
        selectedColor = state.subjectCardColors,
        onChangeColor = { onEvent(DashboardEvent.OnSubjectCardColorChange(it)) }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissListener = { isDeleteSessionDialogOpen = false },
        onConfirmListener = {
            onEvent(DashboardEvent.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            DashBoardScreenTopAppBar()
        }) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardSections(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    subjectCount = state.totalSubjectCount,
                    studiedHours = state.totalStudiedHours.toString(),
                    goalHours = state.totalGoalStudyHours.toString()
                )
            }
            item {
                SubjectCardSection(
                    modifier = Modifier
                        .fillMaxWidth(),
                    subjectList = state.subjects,
                    onAddSubjectButtonClicked = { isAddSubjectDialogOpen = true },
                    onSubjectCardClicked = onSubjectCardClicked
                )
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp, vertical = 20.dp),
                    onClick = onSessionCardClicked
                ) {
                    Text(
                        text = "Start Study Session",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                tasks = tasks,
                emptyTaskText = "You don't have any Tasks . \nPlease add by clicking + on top right",
                onCheckBoxClick = { onEvent(DashboardEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClicked
            )
            studySessionsList(
                "RECENT STUDY SESSIONS",
                sessions = recentSessions,
                emptySessionText = "You don't have any Session . \nYou have completed all tasks",
                onDeleteIcon = {
                    onEvent(DashboardEvent.OnDeleteSessionButtonClick(it))
                    isDeleteSessionDialogOpen = true
                },
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreenTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(title = {
        Text(
            text = "StudyMate", style = typography.headlineMedium
        )
    })
}

@Composable
fun SubjectCardSection(
    modifier: Modifier = Modifier,
    subjectList: List<Subject>,
    onAddSubjectButtonClicked: () -> Unit,
    onSubjectCardClicked: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 19.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(3.dp)
            )
            IconButton(onClick = onAddSubjectButtonClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Add Subject"
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.books),
                contentDescription = "Books Image"
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "You don't have any subjects to read . \nPlease add by clicking + on top right",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
            ) {
                items(subjectList) { subject ->
                    SubjectCard(
                        subjectName = subject.name,
                        gradientColors = subject.colors.map { Color(it) },
                        onClick = { subject.subjectId?.let { onSubjectCardClicked(it) } }
                    )
                }
            }
        }

    }
}

@Composable
fun CountCardSections(
    modifier: Modifier = Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String,
) {
    Row(modifier = modifier.padding(horizontal = 3.dp)) {
        CountCard(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
            headingText = "Subject Count",
            count = "$subjectCount"
        )
        CountCard(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
            headingText = "Studied Hours",
            count = studiedHours
        )
        CountCard(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 7.dp),
            headingText = "Goal Hours",
            count = goalHours
        )
    }
}
