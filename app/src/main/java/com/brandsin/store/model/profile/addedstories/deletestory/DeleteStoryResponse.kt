package com.brandsin.store.model.profile.addedstories.deletestory

import com.google.gson.annotations.SerializedName

data class DeleteStoryResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
