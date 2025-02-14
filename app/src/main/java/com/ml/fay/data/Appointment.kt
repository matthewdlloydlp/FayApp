package com.ml.fay.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Appointment(
    @SerializedName("appointment_id")
    val appointmentId: String = "",
    @SerializedName("appointment_type")
    val appointmentType: String = "",
    @SerializedName("duration_in_minutes")
    val durationInMinutes: Int = 0,
    @SerializedName("end")
    val end: Date,
    @SerializedName("patient_id")
    val patientId: String = "",
    @SerializedName("provider_id")
    val providerId: String = "",
    @SerializedName("recurrence_type")
    val recurrenceType: String = "",
    @SerializedName("start")
    val start: Date,
    @SerializedName("status")
    val status: String = ""
)