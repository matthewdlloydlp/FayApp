package com.ml.fay

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionManager(private val prefs: SharedPreferences) {

    companion object {
        const val USER_TOKEN = "user_token"
    }

    private val _isUserLoggedIn = MutableStateFlow(prefs.getString(USER_TOKEN, null) != null)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun saveAuthToken(token: String) {
        _isUserLoggedIn.tryEmit(true)
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun deleteAuthToken() {
        _isUserLoggedIn.tryEmit(false)
        prefs.edit().remove(USER_TOKEN).apply()
    }

    fun fetchAuthToken(): String? {
        val token = prefs.getString(USER_TOKEN, null)
        token?.let {
            _isUserLoggedIn.tryEmit(true)
        }
        return token
    }
}