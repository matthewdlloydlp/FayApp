package com.ml.fay

import app.cash.turbine.test
import com.ml.fay.data.SignInRequest
import com.ml.fay.repository.AuthRepository
import com.ml.fay.ui.login.LoginStateMessages
import com.ml.fay.ui.login.LoginUiState
import com.ml.fay.ui.login.LoginViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var authRepository: AuthRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `login emits Success state and navigation event on successful login`() = testScope.runTest {
        // Arrange
        val credentials = SignInRequest("test@example.com", "password")
        coEvery { authRepository.login(credentials) } returns true
        val viewModel = LoginViewModel(authRepository)


        // Act
        viewModel.messages.test {
            viewModel.login(credentials)

            // Assert Messages
            assertEquals(LoginStateMessages.LOGIN_SUCCESSFUL, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        // Assert UI State
        viewModel.uiState.test {
            assertEquals(LoginUiState.Success, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        // Assert Navigation
        viewModel.navigationEvents.test {
            viewModel.login(credentials)

            assertEquals(Screen.AppointmentsList.route, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login emits Base state with error message when login fails`() = testScope.runTest {
        // Arrange
        val credentials = SignInRequest("test@example.com", "wrongpassword")
        coEvery { authRepository.login(credentials) } returns false
        val viewModel = LoginViewModel(authRepository)

        // Act
        viewModel.messages.test {
            // since messages is a shared flow we must be collecting before calling login
            viewModel.login(credentials)

            // Assert Messages
            val message = awaitItem()
            println(message)
            assertEquals(LoginStateMessages.SIGN_IN_FAILED, message)
            cancelAndIgnoreRemainingEvents()
        }
        // Assert UI State
        viewModel.uiState.test {
            val res = awaitItem()
            println(res)
            assertEquals(LoginUiState.Base(isLoading = false), res)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login emits Base state with connection error message on exception`() = testScope.runTest {
        // Arrange
        val credentials = SignInRequest("test@example.com", "password")
        coEvery { authRepository.login(credentials) } throws RuntimeException("Network error")
        val viewModel = LoginViewModel(authRepository)

        // Act
        viewModel.messages.test {
            viewModel.login(credentials)

            // Assert Messages
            assertEquals(LoginStateMessages.SIGN_IN_FAILED_CONNECTION, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        // Assert UI State
        viewModel.uiState.test {
            assertEquals(LoginUiState.Base(isLoading = false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}