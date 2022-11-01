package com.brandsin.store.model.profile.addedstories.uploadstory

import com.google.gson.annotations.SerializedName

data class UploadStoryResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
