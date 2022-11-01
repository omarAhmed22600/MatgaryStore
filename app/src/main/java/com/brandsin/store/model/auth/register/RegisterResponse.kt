package com.brandsin.store.model.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("store")
	val store: StoreModel? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: UserModel? = null
)

data class TagsItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class CategoriesItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("item_order")
	val itemOrder: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class StoreModel(

		@field:SerializedName("has_delivery")
		val hasDelivery: Int? = null,
		
		@field:SerializedName("commercial_register")
		val commercialRegister: CommercialRegister? = null,

		@field:SerializedName("account_type")
		val accountType: Any? = null,

		@field:SerializedName("registration_file")
		val registrationFile: Any? = null,

		@field:SerializedName("delivery_time")
		val deliveryTime: Int? = null,

		@field:SerializedName("land_mark")
		val landMark: Any? = null,

		@field:SerializedName("causer_type")
		val causerType: Any? = null,

		@field:SerializedName("approved")
		val approved: Int? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("slug")
		val slug: String? = null,

		@field:SerializedName("lat")
		val lat: String? = null,

		@field:SerializedName("is_closed")
		val isClosed: Int? = null,

		@field:SerializedName("image")
		val image: String? = null,

		@field:SerializedName("thumbnail")
		val thumbnail: String? = null,

		@field:SerializedName("thumbnail_id")
		val thumbnailId: Int? = null,

		@field:SerializedName("images")
		val images: List<ImagesItem?>? = null,

		@field:SerializedName("lng")
		val lng: String? = null,

		@field:SerializedName("min_order_price")
		val minOrderPrice: String? = null,

		@field:SerializedName("building_no")
		val buildingNo: Any? = null,

//	@field:SerializedName("created_by")
//	val createdBy: Int? = null,

//	@field:SerializedName("deleted_at")
//	val deletedAt: Any? = null,

		@field:SerializedName("tags")
		val tags: List<TagsItem?>? = null,

		@field:SerializedName("phone_number2")
		val phoneNumber2: Any? = null,

		@field:SerializedName("min_price_product")
		val minPriceProduct: Any? = null,

		@field:SerializedName("user_id")
		val userId: Int? = null,

		@field:SerializedName("apartment_no")
		val apartmentNo: Any? = null,

		@field:SerializedName("registration_image2")
		val registrationImage2: Any? = null,

		@field:SerializedName("name")
		val name: String? = null,

		@field:SerializedName("registration_image")
		val registrationImage: Any? = null,

//	@field:SerializedName("updated_by")
//	val updatedBy: Int? = null,

		@field:SerializedName("new_flag")
		val newFlag: Int? = null,

		@field:SerializedName("phone_number")
		val phoneNumber: String? = null,

		@field:SerializedName("status")
		val status: String? = null,

		@field:SerializedName("delivery_price")
		val deliveryPrice: String? = null,

		@field:SerializedName("whatsapp")
		val whatsapp: String? = null,

		@field:SerializedName("floor_no")
		val floorNo: Any? = null,

		@field:SerializedName("short_description")
		val shortDescription: Any? = null,

		@field:SerializedName("parking_domain")
		val parkingDomain: Any? = null,

//	@field:SerializedName("created_at")
//	val createdAt: String? = null,

		@field:SerializedName("code_name")
		val codeName: Any? = null,

//	@field:SerializedName("updated_at")
//	val updatedAt: String? = null,

		@field:SerializedName("avg_rating")
		val avgRating: Double? = null,

		@field:SerializedName("categories")
		val categories: List<CategoriesItem?>? = null,

		@field:SerializedName("email")
		val email: Any? = null,

		@field:SerializedName("covers")
		val covers: List<Any?>? = null,

		@field:SerializedName("address")
		val address: Any? = null,

		@field:SerializedName("delivery_distance")
		val deliveryDistance: Int? = null,

		@field:SerializedName("registration_file2")
		val registrationFile2: Any? = null,

		@field:SerializedName("causer_id")
		val causerId: Any? = null,

		@field:SerializedName("offer_flag")
		val offerFlag: Int? = null,

		@field:SerializedName("fixed_commission_price")
		val fixedCommissionPrice: Any? = null,

		@field:SerializedName("is_busy")
		val isBusy: Int? = null,

		@field:SerializedName("is_featured")
		val isFeatured: Int? = null
)

data class ConfirmedAt(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("timezone_type")
	val timezoneType: Int? = null
)

data class ImagesItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class CommercialRegister(

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("url")
		val url: String? = null
)

data class NationalIdItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class UserModel(

	@field:SerializedName("national_id")
	val nationalId: List<NationalIdItem?>? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

//	@field:SerializedName("created_at")
//	val createdAt: String? = null,
//
//	@field:SerializedName("created_by")
//	val createdBy: Int? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("picture_thumb")
	val pictureThumb: String? = null,

//	@field:SerializedName("updated_at")
//	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

//	@field:SerializedName("updated_by")
//	val updatedBy: Int? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("provider_fb")
	val providerFb: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

//	@field:SerializedName("confirmed_at")
//	val confirmedAt: ConfirmedAt? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
