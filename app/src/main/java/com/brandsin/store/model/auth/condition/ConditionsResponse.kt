package com.brandsin.store.model.auth.condition

import com.google.gson.annotations.SerializedName

data class ConditionsResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)