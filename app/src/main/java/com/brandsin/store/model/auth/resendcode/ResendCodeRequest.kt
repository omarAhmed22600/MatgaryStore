package com.brandsin.store.model.auth.resendcode

import com.google.gson.annotations.SerializedName

data class ResendCodeRequest (
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("lang") var lang: String? = null
)