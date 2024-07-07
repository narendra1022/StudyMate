package com.example.studymate.ui.Screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studymate.sessions
import com.example.studymate.subList
import com.example.studymate.ui.Components.DeleteDialog
import com.example.studymate.ui.Components.SubjectListBottomSheet
import com.example.studymate.ui.Components.studySessionsList
import com.example.studymate.ui.ViewModels.SessionViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SessionScreenRoute(navigator: DestinationsNavigator) {
    SessionScreen(
        onBackButtonClicked = { navigator.navigateUp() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val viewModel: SessionViewModel = hiltViewModel()

    var isBottomSheetOpen by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    var isDeleteSessionDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        body = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissListener = { isDeleteSessionDialogOpen = false },
        onConfirmListener = { isDeleteSessionDialogOpen = false }
    )

    SubjectListBottomSheet(
        sheetState = bottomSheetState,
        isOpen = isBottomSheetOpen,
        subjects = subList,
        onDismissRequest = { isBottomSheetOpen = false },
        onSubjectClicked = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                if (!bottomSheetState.isVisible) isBottomSheetOpen = false
            }
        })



    Scaffold(
        topBar = {
            SessionTopAppBar(modifier = modifier, onBackButtonClicked)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                Spacer(modifier = Modifier.height(10.dp))

                TimerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

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
                    Text(text = "English", style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { isBottomSheetOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Subject"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

                ButtonsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    startButtonClick = { /*TODO*/ },
                    cancelButtonClick = { /*TODO*/ }) {

                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

            }

            studySessionsList(
                "STUDY SESSIONS HISTORY",
                sessions = sessions,
                emptySessionText = "You don't have any Session . \nYou have completed all tasks",
                onDeleteIcon = { isDeleteSessionDialogOpen = true }
            )

        }
    }
}

@Composable
fun TimerSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape)
        )
        Text(text = "00:12:20", style = MaterialTheme.typography.headlineMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionTopAppBar(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit
) {
    androidx.compose.material3.TopAppBar(
        title = {
            Text(
                text = "Session",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button"
                )
            }
        }
    )
}

@Composable
private fun ButtonsSection(
    modifier: Modifier,
    startButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    finishButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = cancelButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Cancel"
            )
        }
        Button(onClick = startButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Start"
            )
        }
        Button(onClick = finishButtonClick) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                text = "Finish"
            )
        }
    }
}
