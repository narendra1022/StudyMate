package com.example.studymate.ui.ViewModels

import androidx.lifecycle.ViewModel
import com.example.studymate.ui.Repository.interfaces.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {
}