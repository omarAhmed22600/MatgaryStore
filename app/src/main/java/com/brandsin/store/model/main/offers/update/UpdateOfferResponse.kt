package com.brandsin.store.model.main.offers.update

import com.google.gson.annotations.SerializedName

data class UpdateOfferResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class MediaItem(

	@field:SerializedName("manipulations")
	val manipulations: List<Any?>? = null,

	@field:SerializedName("order_column")
	val orderColumn: Int? = null,

	@field:SerializedName("file_name")
	val fileName: String? = null,

	@field:SerializedName("model_type")
	val modelType: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("model_id")
	val modelId: Int? = null,

	@field:SerializedName("custom_properties")
	val customProperties: CustomProperties? = null,

	@field:SerializedName("disk")
	val disk: String? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("mime_type")
	val mimeType: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("collection_name")
	val collectionName: String? = null
)

data class Data(

	@field:SerializedName("name_en")
	var nameEn: String? = null,
	@field:SerializedName("description_en")
	var descriptionEn: String? = null,

	@field:SerializedName("end_date")
	val endDate: String? = null,

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price_to")
	val priceTo: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("active")
	val active: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("products")
	val products: List<ProductsItem?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
)

data class Pivot(

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("offer_id")
	val offerId: Int? = null
)

data class CustomProperties(

	@field:SerializedName("featured")
	val featured: Boolean? = null,

	@field:SerializedName("root")
	val root: String? = null
)

data class ProductsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("discount")
	val discount: Int? = null,

	@field:SerializedName("files")
	val files: Any? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("video")
	val video: Any? = null,

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null
)
