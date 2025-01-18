package com.ml.fay.ui.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ml.fay.SessionManager
import com.ml.fay.data.Appointment
import com.ml.fay.repository.AppointmentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val appointmentsRepository: AppointmentsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppointmentsUiState>(AppointmentsUiState.Loading)
    val uiState: StateFlow<AppointmentsUiState> = _uiState

    init {
        getAppointments()
    }

    fun getAppointments() {
        viewModelScope.launch {
            try {
                val response = appointmentsRepository.getAppointments()
                if (response.isSuccessful) {
                    response.body()?.let {
                        val (upcoming, past) = it.appointments.partition {
                            it.start.before(Date())
                        }

                        _uiState.emit(
                            AppointmentsUiState.Success(upcoming, past)
                        )
                    } ?: run {
                        // handle null product
                        val errorMsg = "Error: Unable to retrieve product"
                        _uiState.emit(AppointmentsUiState.Error(Exception(errorMsg)))
                    }
                } else {
                    val errorMsg = "Error: ${response.code()} ${response.message()}"
                    _uiState.emit(AppointmentsUiState.Error(Exception(errorMsg)))
                }
            } catch (e: Exception) {
                val errorMsg = "Error: Connection issue"
                _uiState.emit(AppointmentsUiState.Error(Exception(errorMsg)))
            }
        }
    }

    fun handleSignOutClicked() {
        sessionManager.deleteAuthToken()
    }
}

sealed interface AppointmentsUiState {
    object Loading : AppointmentsUiState
    data class Error(val throwable: Throwable) : AppointmentsUiState
    data class Success(
        val upcoming: List<Appointment>,
        val past: List<Appointment>
    ) : AppointmentsUiState
}