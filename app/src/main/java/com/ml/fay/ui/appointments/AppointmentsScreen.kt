package com.ml.fay.ui.appointments

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ml.fay.R
import com.ml.fay.data.Appointment
import com.ml.fay.extensions.toTimeRangeStringWithTimezone
import com.ml.fay.ui.components.DateSquare
import com.ml.fay.ui.components.FayHeader
import com.ml.fay.ui.components.FullScreenDisplayError
import com.ml.fay.ui.components.PageHeader
import com.ml.fay.ui.theme.UiConstants
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
                FullScreenDisplayError {
                    viewModel.getAppointments()
                }
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
    val upcomingListState = rememberLazyListState()
    val pastListState = rememberLazyListState()

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
        0 -> AppointmentsList(futureAppointments, upcomingListState)
        1 -> AppointmentsList(pastAppointments, pastListState)
    }


    // sign out button
    Spacer(Modifier.height(32.dp))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { handleSignOutClicked() },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text(text = stringResource(R.string.sign_out), fontSize = 16.sp)
        }
    }
}

@Composable
internal fun AppointmentsList(
    appointments: List<Appointment>,
    listState: LazyListState
) {
    val expandedStates = remember { mutableStateOf(mutableMapOf<String, Boolean>()) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = UiConstants.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
    ) {
        item {
            Spacer(Modifier.height(16.dp))
        }
        items(appointments.size,
            { index ->
                appointments[index].appointmentId
            }
        ) { index ->
            val appointment = appointments[index]
            AppointmentRow(appointment, expandedStates.value[appointment.appointmentId]) {
                expandedStates.value = expandedStates.value.toMutableMap().apply {
                    this[appointment.appointmentId] = !(this[appointment.appointmentId] ?: false)
                }
            }
        }
    }
}

@Composable
internal fun AppointmentRow(
    appointment: Appointment,
    isExpanded: Boolean?,
    handleAppointmentClicked: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(UiConstants.cardCornerRadius),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { handleAppointmentClicked(appointment.appointmentId) }
            .testTag(appointment.appointmentId)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(
                    UiConstants.cardPadding
                )
        ) {
            Row {
                DateSquare(appointment.start)
                Column(modifier = Modifier.padding(horizontal = UiConstants.cardPadding)) {
                    Text(
                        "${appointment.appointmentType} with Taylor Palmer, RD",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        appointment.start.toTimeRangeStringWithTimezone(appointment.end),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_sync),
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = stringResource(R.string.sync_icon)
                        )
                        Text(
                            appointment.recurrenceType,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = UiConstants.elementSpacing)
                        )
                    }
                }
            }
            if (isExpanded == true) {
                Spacer(Modifier.height(UiConstants.cardPadding))
                Button(
                    onClick = { /* no op */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(UiConstants.buttonCornerRadius)
                ) {
                    Row {
                        Icon(
                            painter = painterResource(R.drawable.ic_video),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = stringResource(R.string.sync_icon),
                            modifier = Modifier.padding(end = UiConstants.elementSpacing)
                        )
                        Text(text = stringResource(R.string.join_zoom), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
