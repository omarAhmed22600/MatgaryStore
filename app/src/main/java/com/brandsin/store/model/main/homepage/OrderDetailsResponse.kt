package com.brandsin.store.model.main.homepage

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.StoreModel
import com.brandsin.store.model.auth.register.UserModel

data class OrderDetailsResponse(

	@field:SerializedName("offers")
	val offers: List<Any?>? = null,

	@field:SerializedName("total")
	val total: List<TotalItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("discount")
	val discount: Any? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null,

	@field:SerializedName("order")
	val order: Order? = null
)

//data class UserData(
//
//	@field:SerializedName("national_id")
//	val nationalId: String? = null,
//
//	@field:SerializedName("name")
//	val name: String? = null,
//
//	@field:SerializedName("last_name")
//	val lastName: String? = null,
//
//	@field:SerializedName("id")
//	val id: Int? = null,
//
//	@field:SerializedName("picture")
//	val picture: String? = null,
//
//	@field:SerializedName("picture_thumb")
//	val pictureThumb: String? = null
//)

data class CreatedAt(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("timezone_type")
	val timezoneType: Int? = null
)

data class ItemsItem(

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("item_options")
	val itemOptions: ItemOptions? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("item_options_value")
	val itemOptionsValue: List<Any?>? = null,

	@field:SerializedName("addons")
	val addons: List<AddonsItem?>? = null,

	@field:SerializedName("user_notes")
	val userNotes: Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("product_name")
	val productName: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("store_name")
	val storeName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sku_code")
	val skuCode: String? = null
)

data class BillingAddress(

	@field:SerializedName("zip")
	val zip: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("address_1")
	val address1: String? = null,

	@field:SerializedName("address_2")
	val address2: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("street_name")
	val streetName: String? = null
)

data class ItemOptions(

	@field:SerializedName("product_options")
	val productOptions: String? = null
)

data class StoreData(

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

	@field:SerializedName("images")
	val images: List<Any?>? = null,

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("min_order_price")
	val minOrderPrice: Any? = null,

	@field:SerializedName("building_no")
	val buildingNo: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("commercial_register")
	val commercialRegister: String? = null,

	@field:SerializedName("phone_number2")
	val phoneNumber2: Any? = null,

	@field:SerializedName("min_price_product")
	val minPriceProduct: String? = null,

	@field:SerializedName("has_delivery")
	val hasDelivery: Int? = null,

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

	@field:SerializedName("updated_by")
	val updatedBy: Int? = null,

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

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("code_name")
	val codeName: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("avg_rating")
	val avgRating: Double? = null,

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

data class Address(

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type_label")
	val typeLabel: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("is_default")
	val isDefault: Int? = null,

	@field:SerializedName("lat")
	val lat: String? = null
)

data class Order(

	@field:SerializedName("order_number")
	val orderNumber: String? = null,

	@field:SerializedName("user_notes")
	val userNotes: String? = null,

	@field:SerializedName("delivery_time")
	val deliveryTime: String? = null,

	@field:SerializedName("street_name")
	val streetName: String? = null,

	@field:SerializedName("billing")
	val billing: Billing? = null,

	@field:SerializedName("city_name")
	val cityName: Any? = null,

	@field:SerializedName("state_name")
	val stateName: Any? = null,

	@field:SerializedName("country_name")
	val countryName: String? = null,

	@field:SerializedName("store_name")
	val storeName: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state_id")
	val stateId: Int? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("discount_id")
	val discountId: Int? = null,

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("comments")
	val comments: List<Any?>? = null,

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("address_id")
	val addressId: Int? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("store")
	val store: StoreModel? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("is_rated")
	val isRated: Boolean? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("user")
	val user: UserModel? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("city_id")
	val cityId: Int? = null
)

data class TotalItem(

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("sub_total")
	val subTotal: String? = null
)

data class Billing(

	@field:SerializedName("payment_status")
	val paymentStatus: String? = null,

	@field:SerializedName("billing_address")
	val billingAddress: BillingAddress? = null,

	@field:SerializedName("payment_reference")
	val paymentReference: String? = null,

	@field:SerializedName("gateway")
	val gateway: String? = null
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
)

