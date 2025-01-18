package com.ml.fay.data
import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token")
    val token: String = ""
)

