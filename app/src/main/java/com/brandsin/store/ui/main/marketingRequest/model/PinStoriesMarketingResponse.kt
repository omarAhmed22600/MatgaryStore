package com.brandsin.store.ui.main.marketingRequest.model

import com.google.gson.annotations.SerializedName

data class PinStoriesMarketingResponse(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("data")
    val value: String? = null
)

