package com.ml.fay

import app.cash.turbine.test
import com.ml.fay.data.Appointment
import com.ml.fay.data.GetAppointmentsResponse
import com.ml.fay.repository.AppointmentsRepository
import com.ml.fay.ui.appointments.AppointmentsUiState
import com.ml.fay.ui.appointments.AppointmentsViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class AppointmentsViewModelTest {

    private lateinit var appointmentsRepository: AppointmentsRepository
    private lateinit var sessionManager: SessionManager

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appointmentsRepository = mockk()
        sessionManager = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `getAppointments emits Success when repository call is successful`() = testScope.runTest {
        // Arrange
        val appointments = listOf(
            Appointment("1", start = Date(1), end = Date(2)),
            Appointment("2", start = Date(3), end = Date(4))
        )
        val response: Response<GetAppointmentsResponse> =
            Response.success(GetAppointmentsResponse(appointments))

        coEvery { appointmentsRepository.getAppointments() } returns response
        val viewModel = AppointmentsViewModel(appointmentsRepository, sessionManager)

        // Act
        viewModel.getAppointments()

        // Assert
        viewModel.uiState.test {
            val successState = awaitItem() as AppointmentsUiState.Success
            assert(successState.past == appointments) // All past due to dates
            assert(successState.upcoming.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAppointments emits Error when repository call fails`() = testScope.runTest {
        // Arrange
        val response = Response.error<GetAppointmentsResponse>(
            500,
            mockk(relaxed = true)
        )
        coEvery { appointmentsRepository.getAppointments() } returns response
        val viewModel = AppointmentsViewModel(appointmentsRepository, sessionManager)
        // Act
        viewModel.getAppointments()

        // Assert
        viewModel.uiState.test {
            val errorState = awaitItem() as AppointmentsUiState.Error
            println(errorState.throwable.message)
            assert(errorState.throwable.message == "Error: 500 Response.error()")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAppointments emits Error on exception`() = testScope.runTest {
        // Arrange
        coEvery { appointmentsRepository.getAppointments() } throws RuntimeException("Network error")
        val viewModel = AppointmentsViewModel(appointmentsRepository, sessionManager)
        // Act
        viewModel.getAppointments()

        // Assert
        viewModel.uiState.test {
            val errorState = awaitItem() as AppointmentsUiState.Error
            assert(errorState.throwable.message == "Error: Connection issue")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleSignOutClicked calls deleteAuthToken`() = testScope.runTest {
        // Arrange
        val viewModel = AppointmentsViewModel(appointmentsRepository, sessionManager)

        // Act
        viewModel.handleSignOutClicked()

        // Assert
        verify { sessionManager.deleteAuthToken() }
    }
}
