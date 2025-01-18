package com.ml.fay.ui.appointments

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ml.fay.R
import com.ml.fay.data.Appointment

@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel = hiltViewModel(),
    handleSignOutClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is AppointmentsUiState.Success -> {
            AppointmentsScreen(appointments = (uiState as AppointmentsUiState.Success).data) {
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


@Composable
internal fun AppointmentsScreen(
    appointments: List<Appointment>,
    handleSignOutClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
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
        item {
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { handleSignOutClicked() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(text = stringResource(R.string.sign_out), fontSize = 16.sp)
            }
        }
    }
}