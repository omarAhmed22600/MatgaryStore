package com.brandsin.store.model.auth.setting.countryId

import com.google.gson.annotations.SerializedName

data class CountryIdResponse(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)
