package com.brandsin.store.ui.main.offersAndFeatures.model

import com.brandsin.store.model.main.offers.add.MediaItem
import com.google.gson.annotations.SerializedName

data class OfferAndFeatureListResponse(

	@field:SerializedName("data")
	val offerAndFeatureItem: List<OfferAndFeatureItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,
)

data class OfferAndFeatureItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)