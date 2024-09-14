package com.brandsin.store.ui.main.categories.model

import com.google.gson.annotations.SerializedName

data class CategoriesListResponse(

    @field:SerializedName("data")
    val categoriesList: List<CategoryItem?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class CategoryItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("name_en")
    val nameEn: String? = null,

    /*
    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("imageEn")
    val imageEn: Any? = null,

    @field:SerializedName("mobileImage")
    val mobileImage: Any? = null,

    @field:SerializedName("cover")
    val cover: Any? = null,

    @field:SerializedName("bannerMobileImage")
    val bannerMobileImage: Any? = null,

    @field:SerializedName("sidebar")
    val sidebar: Any? = null,*/
)
