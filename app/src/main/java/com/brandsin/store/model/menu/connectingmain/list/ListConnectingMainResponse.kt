package com.brandsin.store.model.menu.connectingmain.list

import com.google.gson.annotations.SerializedName

data class ListConnectingMainResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Store(

	@field:SerializedName("min_price_product")
	val minPriceProduct: Any? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("images")
	val images: List<Any?>? = null,

	@field:SerializedName("thumbnail_id")
	val thumbnailId: Any? = null,

	@field:SerializedName("avg_rating")
	val avgRating: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("commercial_register")
	val commercialRegister: Any? = null,

	@field:SerializedName("covers")
	val covers: List<Any?>? = null
)

data class CommercialRegister(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class CoversItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class ImagesItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class DataItem(

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("store")
	val store: Store? = null,

	@field:SerializedName("accept")
	val accept: Int? = null
)
