package com.brandsin.store.model.main.products

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListUnitResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
): Serializable

data class DataItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Serializable
