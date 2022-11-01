package com.brandsin.store.model.auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreTagsResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<StoreTagsItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
): Serializable

data class StoreTagsItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	var isSelected : Boolean = false
): Serializable
