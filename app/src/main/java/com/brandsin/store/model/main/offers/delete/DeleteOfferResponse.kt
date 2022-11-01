package com.brandsin.store.model.main.offers.delete

import com.google.gson.annotations.SerializedName

data class DeleteOfferResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
