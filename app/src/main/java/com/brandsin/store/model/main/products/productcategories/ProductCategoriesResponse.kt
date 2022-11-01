package com.brandsin.store.model.main.products.productcategories

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductCategoriesResponse(

	@field:SerializedName("data")
	val data: List<ProductCategoriesData?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
): Serializable

data class ProductCategoriesData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("child")
	val child: List<ChildItem?>? = null,

	var isSelected : Boolean = false
): Serializable

data class ChildItem(

	@field:SerializedName("cover")
	val cover: Any? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("imageEn")
	val imageEn: Any? = null,

	@field:SerializedName("mobileImage")
	val mobileImage: Any? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("bannerMobileImage")
	val bannerMobileImage: Any? = null,

	@field:SerializedName("sidebar")
	val sidebar: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Serializable
