package com.ml.fay.data

import com.google.gson.annotations.SerializedName

data class GetAppointmentsResponse(
    @SerializedName("appointments")
    val appointments: List<Appointment> = listOf()
)


