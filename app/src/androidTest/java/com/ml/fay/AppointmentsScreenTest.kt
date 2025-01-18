package com.ml.fay

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ml.fay.data.Appointment
import com.ml.fay.ui.appointments.AppointmentsList
import com.ml.fay.ui.appointments.AppointmentsScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class AppointmentsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val futureAppointments = listOf(
        Appointment(
            appointmentId = "1",
            appointmentType = "Initial Consultation",
            start = Date(1),
            end = Date(1000),
            recurrenceType = "None"
        )
    )
    private val pastAppointments = listOf(
        Appointment(
            appointmentId = "2",
            appointmentType = "Follow-up",
            start = Date(2000),
            end = Date(3000),
            recurrenceType = "Weekly"
        )
    )

    @Test
    fun displayTabs() {
        composeTestRule.activity.setContent {
            Column {
                AppointmentsScreen(
                    futureAppointments = futureAppointments,
                    pastAppointments = pastAppointments,
                    handleSignOutClicked = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Upcoming")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Past")
            .assertIsDisplayed()
    }

    @Test
    fun displayFutureAppointments() {
        composeTestRule.activity.setContent {
            Column {
                AppointmentsScreen(
                    futureAppointments = futureAppointments,
                    pastAppointments = pastAppointments,
                    handleSignOutClicked = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Upcoming")
            .performClick()

        futureAppointments.forEach { appointment ->
            composeTestRule
                .onNodeWithTag(appointment.appointmentId)
                .assertIsDisplayed()
        }
    }

    @Test
    fun displayPastAppointments() {
        composeTestRule.activity.setContent {
            Column {
                AppointmentsScreen(
                    futureAppointments = futureAppointments,
                    pastAppointments = pastAppointments,
                    handleSignOutClicked = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Past")
            .performClick()

        pastAppointments.forEach { appointment ->
            composeTestRule
                .onNodeWithTag(appointment.appointmentId)
                .assertIsDisplayed()
        }
    }

    @Test
    fun handleSignOutButtonClicked() {
        var signOutClicked = false
        composeTestRule.activity.setContent {
            Column {
                AppointmentsScreen(
                    futureAppointments = futureAppointments,
                    pastAppointments = pastAppointments,
                    handleSignOutClicked = { signOutClicked = true }
                )
            }
        }

        composeTestRule
            .onNodeWithText("Sign Out")
            .performClick()

        assert(signOutClicked)
    }

    @Test
    fun expandAndCollapseAppointmentRow() {
        composeTestRule.activity.setContent {
            AppointmentsList(
                appointments = futureAppointments,
                listState = rememberLazyListState()
            )
        }

        val appointment = futureAppointments.first()

        composeTestRule
            .onNodeWithText(appointment.recurrenceType)
            .performClick()

        composeTestRule
            .onNodeWithText("Join Zoom")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(appointment.recurrenceType)
            .performClick()

        composeTestRule
            .onNodeWithText("Join Zoom")
            .assertDoesNotExist()
    }
}