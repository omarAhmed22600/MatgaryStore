package com.brandsin.store.model.auth.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("lang") var lang: String? = null
)