package com.brandsin.store.model

import com.google.gson.annotations.SerializedName

data class IntroResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class DataItem(

	@field:SerializedName("app")
	val app: String? = null,

	@field:SerializedName("image_ar")
	val imageAr: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("introduction")
	val introduction: String? = null,

	@field:SerializedName("image_en")
	val imageEn: String? = null,

	@field:SerializedName("item_order")
	val itemOrder: Int? = null
)
