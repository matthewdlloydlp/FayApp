package com.ml.fay.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ml.fay.R
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

    private val _messages = MutableSharedFlow<LoginStateMessages>()
    val messages: SharedFlow<LoginStateMessages> = _messages

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun login(credentials: SignInRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(LoginUiState.Base(isLoading = true))
            try {
                val result = authRepository.login(credentials)
                if (result) {
                    _uiState.emit(LoginUiState.Success)
                    _messages.emit(LoginStateMessages.LOGIN_SUCCESSFUL)
                    _navigationEvents.emit(Screen.AppointmentsList.route)
                } else {
                    _uiState.emit(LoginUiState.Base(isLoading = false))
                    _messages.emit(LoginStateMessages.SIGN_IN_FAILED)
                }
            } catch (e: Exception) {
                _uiState.emit(LoginUiState.Base(isLoading = false))
                _messages.emit(LoginStateMessages.SIGN_IN_FAILED_CONNECTION)
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

enum class LoginStateMessages(val id: Int) {
    LOGIN_SUCCESSFUL(R.string.login_successful),
    SIGN_IN_FAILED(R.string.sign_in_failed),
    SIGN_IN_FAILED_CONNECTION(R.string.sign_in_failed_connection_error)
}