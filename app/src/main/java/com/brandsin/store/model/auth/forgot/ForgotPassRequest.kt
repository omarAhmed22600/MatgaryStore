package com.brandsin.store.model.auth.forgot

import com.google.gson.annotations.SerializedName

data class ForgotPassRequest(
    @SerializedName("phone_email") var phone_email: String? = null,
    @SerializedName("lang") var lang: String? = null
)