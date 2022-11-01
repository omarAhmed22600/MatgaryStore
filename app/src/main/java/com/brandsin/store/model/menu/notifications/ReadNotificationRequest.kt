package com.brandsin.store.model.menu.notifications

import com.google.gson.annotations.SerializedName

data class ReadNotificationRequest(
    @SerializedName("notification_id")
    var notificationId: Int? = null,

    @SerializedName("order_id")
    var orderId: Int? = null,

    @SerializedName("user_id")
    var userId: Int? = null
)