package com.ml.fay.repository

import android.util.Log
import com.ml.fay.SessionManager
import com.ml.fay.api.FayApi
import com.ml.fay.data.SignInRequest
import javax.inject.Inject


interface AuthRepository {
    suspend fun login(credentials: SignInRequest): Boolean
}


class DefaultAuthRepository @Inject constructor(
    private val fayApi: FayApi,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun login(credentials: SignInRequest): Boolean {

        val response = fayApi.signIn(credentials)
        if (response.isSuccessful) {
            response.body()?.let {
                sessionManager.saveAuthToken(it.token)
                return true
            }
        } else {
            // Log.d("AUTH", "failure: ${response.code()} ${response.body()}")
        }
        // since the endpoint just returns a 401 we can just return false,
        // if the response had other error info we could pass that to the viewmodel

        return false
    }
}