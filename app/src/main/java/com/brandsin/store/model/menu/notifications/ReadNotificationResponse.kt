package com.brandsin.user.model.menu.notifications

import com.google.gson.annotations.SerializedName

data class ReadNotificationResponse(

	@field:SerializedName("success")
	val isSuccess: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
