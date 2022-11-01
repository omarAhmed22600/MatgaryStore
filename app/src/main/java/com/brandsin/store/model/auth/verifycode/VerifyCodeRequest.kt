package com.brandsin.store.model.auth.verifycode

import com.google.gson.annotations.SerializedName

data class VerifyCodeRequest(
    @SerializedName("phone_number") var phone_number: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("user_id") var user_id: String? = null,
    @SerializedName("lang") var lang: String? = null
)