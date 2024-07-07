package com.example.studymate.ui.StateValues

import androidx.compose.ui.graphics.Color
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Subject

data class DashBoardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalStudyHours: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val session: Session? = null
)