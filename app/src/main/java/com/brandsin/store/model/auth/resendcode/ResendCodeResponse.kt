package com.brandsin.store.model.auth.resendcode

import com.google.gson.annotations.SerializedName

data class ResendCodeResponse(
	@field:SerializedName("success")
	val success: Boolean? = null,
	@field:SerializedName("error")
	val error: String? = null,
	@field:SerializedName("code")
	val code: Int? = null,
	@field:SerializedName("user_id")
	val user_id: Int? = null
)
