package com.brandsin.user.model.menu.notifications

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("data")
	val notificationsList: List<NotificationItem?>? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null
)

data class NotificationItem(

	@field:SerializedName("message_ar")
	val messageAr: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("read_at")
	val readAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("order_id")
	val orderId: Int? = null,

)
