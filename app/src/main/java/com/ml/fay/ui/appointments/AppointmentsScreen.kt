package com.ml.fay.ui.appointments

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ml.fay.R
import com.ml.fay.Screen
import com.ml.fay.data.Appointment
import com.ml.fay.ui.components.FayHeader
import com.ml.fay.ui.components.PageHeader
import com.ml.fay.ui.components.UiConstants
import com.ml.fay.ui.components.UnderlinedTabView

@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel = hiltViewModel(),
    handleSignOutClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        FayHeader()
        PageHeader(stringResource(R.string.appointments))


        when (uiState) {
            is AppointmentsUiState.Success -> {
                AppointmentsScreen(
                    futureAppointments = (uiState as AppointmentsUiState.Success).upcoming,
                    pastAppointments = (uiState as AppointmentsUiState.Success).past
                ) {
                    viewModel.handleSignOutClicked()
                    Toast.makeText(
                        context,
                        context.getString(R.string.signed_out),
                        Toast.LENGTH_SHORT
                    ).show()
                    handleSignOutClicked()
                }
            }

            is AppointmentsUiState.Error -> {
//            FullScreenDisplayError {
//                viewModel.getDeals()
//            }
                Text("error")
            }

            AppointmentsUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
internal fun ColumnScope.AppointmentsScreen(
    futureAppointments: List<Appointment>,
    pastAppointments: List<Appointment>,
    handleSignOutClicked: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.upcoming), stringResource(R.string.past))

    Box(contentAlignment = Alignment.BottomCenter) {
        UnderlinedTabView(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index -> selectedTabIndex = index }
        )
        HorizontalDivider(thickness = 1.dp)
    }
    // Show the content for the selected tab
    when (selectedTabIndex) {
        0 -> AppointmentsList(futureAppointments)
        1 -> AppointmentsList(pastAppointments)
    }


    Spacer(Modifier.height(32.dp))
    Button(
        onClick = { handleSignOutClicked() },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
    ) {
        Text(text = stringResource(R.string.sign_out), fontSize = 16.sp)
    }
}

@Composable
internal fun AppointmentsList(
    appointments: List<Appointment>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(appointments.size,
            { index ->
                appointments[index].appointmentId
            }
        ) { index ->
            val appointment = appointments[index]
            Text(appointment.appointmentId)
        }
    }
}

@Composable
internal fun AppointmentRow(
    appointment: Appointment
) {
    Card(
        shape = RoundedCornerShape(UiConstants.cardCornerRadius),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
    ) {

    }
}
