package com.brandsin.store.ui.main.discountCoupon.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CouponListResponse(

    @field:SerializedName("data")
    val couponItem: ArrayList<CouponItem?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

data class CouponItem(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("store_id")
    val storeId: Int? = null,

    @field:SerializedName("count_used")
    val countUsed: Int? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("start")
    val start: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("expiry")
    val expiry: String? = null,

    @field:SerializedName("value")
    val value: String? = null
)
