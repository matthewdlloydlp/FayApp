package com.ml.fay.api

import com.ml.fay.data.GetAppointmentsResponse
import com.ml.fay.data.SignInRequest
import com.ml.fay.data.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FayApi {

    @POST("signin")
    suspend fun signIn(@Body credentials: SignInRequest): Response<SignInResponse>

    @GET("appointments")
    suspend fun getAppointments(): Response<GetAppointmentsResponse>
}
