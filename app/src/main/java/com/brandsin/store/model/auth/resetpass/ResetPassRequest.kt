package com.brandsin.store.model.auth.resetpass

import com.google.gson.annotations.SerializedName

data class  ResetPassRequest (
    @SerializedName("phone_email") var phone_email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("user_id") var user_id: String? = null,
    @SerializedName("lang") var lang: String? = null
)