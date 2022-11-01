package com.brandsin.store.model.main.offers.add

import com.google.gson.annotations.SerializedName

data class OfferAddProductResponse(

    @field:SerializedName("total")
	val total: Int? = null,

    @field:SerializedName("data")
	val data: List<DataItem?>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("limit")
	val limit: Int? = null
)

data class GlobalAttributeItem(

	@field:SerializedName("display_order")
	val displayOrder: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("required")
	val required: Int? = null
)

data class DataItem(

    @field:SerializedName("allowed_quantity")
	val allowedQuantity: Int? = null,

    @field:SerializedName("original_price")
	val originalPrice: String? = null,

    @field:SerializedName("created_at")
	val createdAt: String? = null,

    @field:SerializedName("product_status")
	val productStatus: Any? = null,

    @field:SerializedName("discount")
	val discount: String? = null,

    @field:SerializedName("custom_properties")
	val customProperties: String? = null,

    @field:SerializedName("video")
	val video: Any? = null,

    @field:SerializedName("media")
	val media: List<MediaItemD?>? = null,

    @field:SerializedName("type")
	val type: String? = null,

    @field:SerializedName("inventory")
	val inventory: String? = null,

    @field:SerializedName("inventory_value")
	val inventoryValue: Any? = null,

    @field:SerializedName("external_url")
	val externalUrl: Any? = null,

    @field:SerializedName("updated_at")
	val updatedAt: String? = null,

    @field:SerializedName("rate")
	val rate: String? = null,

    @field:SerializedName("store_name")
	val storeName: String? = null,

    @field:SerializedName("media_id")
	val mediaId: Int? = null,

    @field:SerializedName("id")
	val id: Int? = null,

    @field:SerializedName("store_id")
	val storeId: Int? = null,

    @field:SerializedName("image")
	val image: String? = null,

    @field:SerializedName("images")
	val images: List<String?>? = null,

    @field:SerializedName("address")
	val address: Any? = null,

    @field:SerializedName("file_name")
	val fileName: String? = null,

    @field:SerializedName("global_attribute")
	val globalAttribute: List<Any?>? = null,

    @field:SerializedName("sale_price")
	val salePrice: String? = null,

    @field:SerializedName("sku_count")
	val skuCount: Int? = null,

    @field:SerializedName("user_id")
	val userId: Int? = null,

    @field:SerializedName("name")
	val name: String? = null,

    @field:SerializedName("files")
	val files: Any? = null,

    @field:SerializedName("sale_by_phone")
	val saleByPhone: Any? = null,

    @field:SerializedName("sku_code")
	val skuCode: String? = null,

    @field:SerializedName("wishlists")
	val wishlists: Int? = null
)

data class MediaItemD(

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
	val customProperties: CustomPropertiesD? = null,

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

data class CustomPropertiesD(

	@field:SerializedName("featured")
	val featured: Boolean? = null,

	@field:SerializedName("root")
	val root: String? = null
)
