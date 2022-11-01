package com.brandsin.user.model.menu.settings

import com.google.gson.annotations.SerializedName

data class PhoneNumberResponse(

	@field:SerializedName("data")
	val phoneNumber: String? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null
)

data class PhoneDataItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)
