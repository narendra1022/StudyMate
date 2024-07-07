package com.example.studymate.ui.Screens.DashBoard

import androidx.compose.ui.graphics.Color
import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Task

sealed class DashboardEvent {

    data object SaveSubject : DashboardEvent()
    data object DeleteSession : DashboardEvent()

    // Iam using  data class because , these classes are handling user inputs

    data class OnDeleteSessionButtonClick(val session: Session) : DashboardEvent()
    data class OnTaskIsCompleteChange(val task: Task) : DashboardEvent()
    data class OnSubjectCardColorChange(val colors: List<Color>) : DashboardEvent()
    data class OnSubjectNameChange(val name: String) : DashboardEvent()
    data class OnGoalStudyHoursChange(val hours: String) : DashboardEvent()
}