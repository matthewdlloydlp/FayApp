package com.ml.fay.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ml.fay.Screen
import com.ml.fay.data.SignInRequest
import com.ml.fay.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Base())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun login(credentials: SignInRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(LoginUiState.Base(isLoading = true))
            val result = authRepository.login(credentials)
            Log.d("matt123", "login result: $result")
            if (result) {
                _uiState.emit(LoginUiState.Success)
                _messages.emit("Login Successful")
                _navigationEvents.emit(Screen.AppointmentsList.route)
            } else {
                _uiState.emit(LoginUiState.Base(isLoading = false))
                _messages.emit("Sign in Failed")
            }
        }
    }

}

sealed interface LoginUiState {
    object Success : LoginUiState
    data class Base(
        val isLoading: Boolean = false,
    ) : LoginUiState
}