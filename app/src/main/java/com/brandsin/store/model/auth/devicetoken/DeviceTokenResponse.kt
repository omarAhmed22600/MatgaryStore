package com.brandsin.store.model.auth.devicetoken

import com.google.gson.annotations.SerializedName

data class DeviceTokenResponse(

	@field:SerializedName("success")
	val success: Boolean? = null
)
