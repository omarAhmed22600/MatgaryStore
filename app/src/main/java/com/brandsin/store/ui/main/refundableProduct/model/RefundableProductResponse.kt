package com.brandsin.store.ui.main.refundableProduct.model

import com.google.gson.annotations.SerializedName

data class RefundableProductResponse(

	@field:SerializedName("data")
	val refundableProductList: List<RefundableProductItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SkusItem(

	@field:SerializedName("allowed_quantity")
	val allowedQuantity: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("free_shipping")
	val freeShipping: Int? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("inventory")
	val inventory: String? = null,

	@field:SerializedName("sale_price")
	val salePrice: String? = null,

	@field:SerializedName("free_refunding")
	val freeRefunding: Int? = null,

	@field:SerializedName("regular_price")
	val regularPrice: String? = null,

	@field:SerializedName("inventory_value")
	val inventoryValue: Any? = null,

	@field:SerializedName("shipping")
	val shipping: Any? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("name")
	val name: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("secured")
	val secured: Int? = null,

	@field:SerializedName("unit_id")
	val unitId: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RefundableProductItem(

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("today_offers")
	val todayOffers: Int? = null,

	@field:SerializedName("discount")
	val discount: Any? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("external_url")
	val externalUrl: Any? = null,

	@field:SerializedName("admin_status")
	val adminStatus: String? = null,

	@field:SerializedName("refundable_status")
	val refundableStatus: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("slug_ar")
	val slugAr: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("deep_link")
	val deepLink: Any? = null,

	@field:SerializedName("whatsapp_number")
	val whatsappNumber: Any? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("meta_title")
	val metaTitle: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("brand_id")
	val brandId: Any? = null,

	@field:SerializedName("is_used")
	val isUsed: Int? = null,

	@field:SerializedName("order_item_id")
	val orderItemId: Int? = null,

	@field:SerializedName("meta_description")
	val metaDescription: Any? = null,

	@field:SerializedName("user_id")
	val userId: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Int? = null,

	@field:SerializedName("files")
	val files: Any? = null,

	@field:SerializedName("refundable_id")
	val refundableId: Int? = null,

	@field:SerializedName("order_id")
	val orderId: Int? = null,

	@field:SerializedName("country_id")
	val countryId: Any? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("city_id")
	val cityId: Any? = null,

	@field:SerializedName("code")
	val code: Any? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("is_favorite")
	val isFavorite: Boolean? = null,

	@field:SerializedName("skus")
	val skus: List<Any?>? = null,

	@field:SerializedName("refund_days")
	val refundDays: Int? = null,

	@field:SerializedName("order_number")
	val orderNumber: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("product_status")
	val productStatus: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("video")
	val video: Any? = null,

	@field:SerializedName("meta_keywords")
	val metaKeywords: Any? = null,

	@field:SerializedName("related_products")
	val relatedProducts: Any? = null,

	@field:SerializedName("video_url")
	val videoUrl: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("avg_rating")
	val avgRating: Double? = null,

	@field:SerializedName("is_refund")
	val isRefund: Int? = null,

	@field:SerializedName("views")
	val views: Int? = null,

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("images_ids")
	val imagesIds: List<ImagesIdsItem?>? = null,

	@field:SerializedName("address")
	val address: Any? = null,

	@field:SerializedName("deep_link_ar")
	val deepLinkAr: Any? = null,

	@field:SerializedName("description_en")
	val descriptionEn: String? = null,

	@field:SerializedName("refundable_date")
	val refundableDate: String? = null,

	@field:SerializedName("is_refundable")
	val isRefundable: Boolean? = null,

	@field:SerializedName("is_shareable")
	val isShareable: Int? = null,

	@field:SerializedName("sale_by_phone")
	val saleByPhone: Any? = null,

	@field:SerializedName("properties")
	val properties: List<Any?>? = null,

	@field:SerializedName("is_featured")
	val isFeatured: Boolean? = null,

	@field:SerializedName("name_en")
	val nameEn: String? = null
)

data class ImagesIdsItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)
