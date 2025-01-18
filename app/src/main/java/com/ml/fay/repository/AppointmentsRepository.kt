package com.ml.fay.repository

import com.ml.fay.api.FayApi
import com.ml.fay.data.GetAppointmentsResponse
import retrofit2.Response
import javax.inject.Inject

interface AppointmentsRepository {

    suspend fun getAppointments(): Response<GetAppointmentsResponse>
}


class DefaultAppointmentsRepository @Inject constructor(
    private val fayApi: FayApi
) : AppointmentsRepository {
    override suspend fun getAppointments(): Response<GetAppointmentsResponse> {
        // in the future could save these to room and then viewmodel would just observe them from
        // room and api would update them there
        return fayApi.getAppointments()
    }
}