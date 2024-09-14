package com.brandsin.store.ui.main.discountCoupon.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateAndUpdateCouponResponse(

    @field:SerializedName("data")
    val data: CouponItem? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,
)
