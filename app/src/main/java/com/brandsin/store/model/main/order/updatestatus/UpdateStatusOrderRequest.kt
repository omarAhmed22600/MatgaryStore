package com.brandsin.store.model.main.order.updatestatus

import com.google.gson.annotations.SerializedName

data class UpdateStatusOrderRequest(

    @SerializedName("order_id")
    var orderId: Int? = null,

    @SerializedName("status")
    var status: String? = null
)