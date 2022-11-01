package com.brandsin.store.model.main.products.delete

import com.google.gson.annotations.SerializedName

data class DeleteProductResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
