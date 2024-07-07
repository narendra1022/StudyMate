package com.example.studymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Subject
import com.example.studymate.ui.Models.Task
import com.example.studymate.ui.Screens.NavGraphs
import com.example.studymate.ui.theme.StudyMateTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyMateTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

val subList = listOf(
    Subject("English", 2.0f, Subject.subjectCardColors[0].map { it.toArgb() }, 0),
    Subject("English", 2.0f, Subject.subjectCardColors[0].map { it.toArgb() }, 1),
    Subject("English", 2.0f, Subject.subjectCardColors[0].map { it.toArgb() }, 2),
    Subject("English", 2.0f, Subject.subjectCardColors[0].map { it.toArgb() }, 3),
    Subject("English", 2.0f, Subject.subjectCardColors[0].map { it.toArgb() }, 4),
)

val tasks = listOf(
    Task(
        "I have to finish this two palylist by today night",
        "Simple",
        1233,
        1,
        "English",
        false,
        1,
        1
    ),
    Task(
        "I have to finish this two palylist by today night",
        "Simple",
        1233,
        1,
        "English",
        true, 1,
        1
    ),
    Task(
        "I have to finish this two palylist by today night",
        "Simple",
        1233,
        1,
        "English",
        true, 1,
        1
    ),
)

val sessions = listOf(
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "Physics",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "Maths",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    ),
    Session(
        relatedToSubject = "English",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0
    )
)
