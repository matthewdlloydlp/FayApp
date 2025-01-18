package com.ml.fay.data

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("password")
    val password: String = "",
    @SerializedName("username")
    val username: String = ""
)


