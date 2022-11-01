package com.brandsin.store.model.auth.resetpass

import com.google.gson.annotations.SerializedName

data class ResetPassResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,
	@field:SerializedName("error")
	val error: String? = null

)
