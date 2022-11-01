package com.brandsin.store.model.menu.connectingmain.accept

import com.google.gson.annotations.SerializedName

data class AcceptConnectingMainResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
