package com.brandsin.store.model.auth.forgot

import com.google.gson.annotations.SerializedName

data class ForgotPassResponse(
	@field:SerializedName("success")
	val success: Boolean? = null,
	@field:SerializedName("error")
	val error: String? = null,
	@field:SerializedName("code")
	val code: Int? = null,
	@field:SerializedName("user_id")
	val user_id: Int? = null
)
