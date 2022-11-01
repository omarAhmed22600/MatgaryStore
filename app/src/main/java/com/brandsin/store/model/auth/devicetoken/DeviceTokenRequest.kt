package com.brandsin.store.model.auth.devicetoken

import com.google.gson.annotations.SerializedName

data class DeviceTokenRequest (
        @SerializedName("user_id") var user_id: String? = null,
        @SerializedName("token") var token: String? = null,
        @SerializedName("type") var type: String? = null
)