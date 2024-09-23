package com.brandsin.store.model.main.products.list

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.brandsin.store.R
import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.main.products.update.SkuUpdateProductItem
import java.io.Serializable

data class ListProductsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class AddonsItem(

		@field:SerializedName("store_id")
		val storeId: Int? = null,

		@field:SerializedName("price")
		val price: Int? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("id")
		val id: Int? = null
): Serializable

data class SkusItem(

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

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("secured")
	val secured: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
): Serializable


data class Data(

	@field:SerializedName("min_price_product")
	val minPriceProduct: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("products_categories")
	val productsCategories: List<ProductCategoriesData?>? = null,

	@field:SerializedName("avg_rating")
	val avgRating: Double? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("delivery_time")
	val deliveryTime: Int? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null,

	@field:SerializedName("delivery_price")
	val deliveryPrice: Int? = null,

	@field:SerializedName("covers")
	val covers: List<Any?>? = null,

	@field:SerializedName("products")
	val products: ArrayList<ProductsItem?>? = null
): Serializable

data class ProductCategoriesData (
	@field:SerializedName("id")
	val id : Int,
	@field:SerializedName("name")
	val name : String,
	@field:SerializedName("image")
	val image : String,
	@field:SerializedName("thumbnail")
	val thumbnail : String,
	@field:SerializedName("imageEn")
	val imageEn : String,
	@field:SerializedName("bannerMobileImage")
	val bannerMobileImage : String,
	@field:SerializedName("mobileImage")
	val mobileImage : String,
	@field:SerializedName("sidebar") val
	sidebar : String,
	@field:SerializedName("cover")
	val cover : String ,

	var isSelected : Boolean = false
): Serializable

data class CategoriesItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	var isSelected : Boolean = false
): Serializable

data class ImagesIdsItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
): Serializable

data class ProductsItem(

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
	val skus: List<SkuUpdateProductItem?>? = null,

	@field:SerializedName("addons")
	val addons: List<AddonsItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("product_status")
	val productStatus: String? = null,

	@field:SerializedName("caption")
	val caption: Any? = null,

	@field:SerializedName("today_offers")
	val todayOffers: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("discount")
	val discount: String? = null,

	@field:SerializedName("video")
	val video: Any? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

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

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null,

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
	val brandId: Any? = null,

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
	var status: String? = null,

	@field:SerializedName("city_id")
	val cityId: Any? = null,

	var isSelected: Boolean = false
): Serializable {
	fun getStatusColor() :Int = when(status) {
		"active" -> com.google.android.libraries.places.R.color.quantum_googgreen300
		else -> R.color.order_rejected_color
	}
	fun getStatusText(v: View):String {
		val context = v.context
		return when (status) {
			"active" -> context.getString(R.string.active)
			else -> context.getString(R.string.disabled)
		}
	}
}
