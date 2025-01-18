package com.ml.fay

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager
): ViewModel() {

    val isUserLoggedIn: StateFlow<Boolean> = sessionManager.isUserLoggedIn
}