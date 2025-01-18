package com.ml.fay

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ml.fay.ui.appointments.AppointmentsScreen
import com.ml.fay.ui.login.LoginScreen

@Composable
fun MainNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isAuthenticated by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()
    val initialRoute =
        if (isAuthenticated) Screen.AppointmentsList.route else Screen.LoginScreen.route

    NavHost(navController = navController, startDestination = initialRoute) {
        composable(Screen.LoginScreen.route) {
            LoginScreen {
                Log.d("matt123", "Main Navigation, nav to appointments")
                navController.navigate(Screen.AppointmentsList.route)
            }
        }
        composable(
            route = Screen.AppointmentsList.route
        ) {
            AppointmentsScreen {
                navController.navigate(Screen.LoginScreen.route)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login")
    object AppointmentsList : Screen("appointments")
}