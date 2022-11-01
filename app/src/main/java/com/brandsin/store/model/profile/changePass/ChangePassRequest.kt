package com.brandsin.store.model.profile.changePass

import com.google.gson.annotations.SerializedName

data class ChangePassRequest (
    @SerializedName("user_id") var user_id: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("new_password") var new_password: String? = null,
    @SerializedName("lang") var lang: String? = null
)