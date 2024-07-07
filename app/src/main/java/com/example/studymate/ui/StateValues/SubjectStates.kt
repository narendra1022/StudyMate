package com.example.studymate.ui.StateValues

import androidx.compose.ui.graphics.Color
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Subject
import com.example.studymate.ui.Models.Task

data class SubjectStates(
    val currentSubjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val studiedHours: Float = 0f,
    val progress: Float = 0f,
    val recentSessions: List<Session> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val session: Session? = null
)