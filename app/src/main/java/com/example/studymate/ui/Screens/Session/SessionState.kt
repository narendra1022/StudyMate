package com.example.studymate.ui.Screens.Session

import com.example.studymate.ui.Models.Session
import com.example.studymate.ui.Models.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)