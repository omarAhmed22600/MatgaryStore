package com.brandsin.store.model

import com.google.gson.annotations.SerializedName

class MessageResponse {

    @field:SerializedName("success")
    val success: Boolean? = null

    @field:SerializedName("message")
    val message: String? = null
}