package com.brandsin.store.model.main.products.update

import com.google.gson.annotations.SerializedName

data class UpdateProductResponse(

        @field:SerializedName("data")
	val data: Data? = null,

        @field:SerializedName("success")
	val isSuccess: Boolean? = null,

        @field:SerializedName("message")
	val message: String? = null
)

data class ImagesIdsItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Data(

	@field:SerializedName("name_en")
	var nameEn: String? = null,
	@field:SerializedName("description_en")
	var descriptionEn: String? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("images_ids")
	val imagesIds: List<ImagesIdsItem?>? = null,

	@field:SerializedName("code")
	val code: Any? = null,

	@field:SerializedName("flag")
	val flag: Int? = null,

	@field:SerializedName("skus")
	val skus: List<DeleteSkusItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("product_status")
	val productStatus: String? = null,

	@field:SerializedName("caption")
	val caption: String? = null,

	@field:SerializedName("today_offers")
	val todayOffers: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("discount")
	val discount: String? = null,

	@field:SerializedName("video")
	val video: Any? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("meta_keywords")
	val metaKeywords: Any? = null,

	@field:SerializedName("related_products")
	val relatedProducts: Any? = null,

	@field:SerializedName("external_url")
	val externalUrl: Any? = null,

	@field:SerializedName("video_url")
	val videoUrl: Any? = null,

	@field:SerializedName("shipping")
	val shipping: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("admin_status")
	val adminStatus: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("slug_ar")
	val slugAr: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("deep_link")
	val deepLink: Any? = null,

	@field:SerializedName("views")
	val views: Int? = null,

	@field:SerializedName("whatsapp_number")
	val whatsappNumber: Any? = null,

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("address")
	val address: Any? = null,

	@field:SerializedName("meta_title")
	val metaTitle: Any? = null,

	@field:SerializedName("deep_link_ar")
	val deepLinkAr: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("brand_id")
	val brandId: Int? = null,

	@field:SerializedName("is_used")
	val isUsed: Int? = null,

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

	@field:SerializedName("sale_by_phone")
	val saleByPhone: Any? = null,

	@field:SerializedName("country_id")
	val countryId: Any? = null,

	@field:SerializedName("properties")
	val properties: Any? = null,

	@field:SerializedName("is_featured")
	val isFeatured: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("city_id")
	val cityId: Any? = null
)

data class DeleteSkusItem(

	@field:SerializedName("allowed_quantity")
	val allowedQuantity: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("free_shipping")
	val freeShipping: Int? = null,

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

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("secured")
	val secured: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
