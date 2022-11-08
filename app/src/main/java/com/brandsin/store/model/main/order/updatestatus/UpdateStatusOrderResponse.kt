package com.brandsin.store.model.main.order.updatestatus

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.StoreModel

data class UpdateStatusOrderResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,


	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Country(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null
)

data class ShippingAddress(

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

data class Data(

		@field:SerializedName("store_id")
		val storeId: Int? = null,

		@field:SerializedName("amount")
		val amount: String? = null,

		@field:SerializedName("driver_id")
		val driverId: Any? = null,

		@field:SerializedName("orders_key")
		val ordersKey: String? = null,

		@field:SerializedName("comments")
		val comments: List<Any?>? = null,

		@field:SerializedName("order_number")
		val orderNumber: String? = null,

		@field:SerializedName("user_notes")
		val userNotes: Any? = null,

		@field:SerializedName("address_id")
		val addressId: Int? = null,

		@field:SerializedName("created_at")
		val createdAt: String? = null,

		@field:SerializedName("device_type")
		val deviceType: String? = null,

		@field:SerializedName("delivery_time")
		val deliveryTime: String? = null,

		@field:SerializedName("store")
		val store: StoreModel? = null,

		@field:SerializedName("created_by")
		val createdBy: Int? = null,

		@field:SerializedName("deleted_at")
		val deletedAt: Any? = null,

		@field:SerializedName("billing")
		val billing: Billing? = null,

		@field:SerializedName("shipping")
		val shipping: Shipping? = null,

		@field:SerializedName("updated_at")
		val updatedAt: String? = null,

		@field:SerializedName("user_id")
		val userId: Int? = null,

		@field:SerializedName("is_rated")
		val isRated: Boolean? = null,

		@field:SerializedName("updated_by")
		val updatedBy: Int? = null,

		@field:SerializedName("currency")
		val currency: String? = null,

		@field:SerializedName("id")
		val id: Int? = null,

		@field:SerializedName("status")
		val status: String? = null,

		@field:SerializedName("discount_id")
		val discountId: Int? = null
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

data class Store(

	@field:SerializedName("floor_no")
	val floorNo: Any? = null,

	@field:SerializedName("short_description")
	val shortDescription: Any? = null,

	@field:SerializedName("account_type")
	val accountType: Any? = null,

	@field:SerializedName("parking_domain")
	val parkingDomain: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("registration_file")
	val registrationFile: Any? = null,

	@field:SerializedName("delivery_time")
	val deliveryTime: Int? = null,

	@field:SerializedName("code_name")
	val codeName: Any? = null,

	@field:SerializedName("land_mark")
	val landMark: Any? = null,

	@field:SerializedName("causer_type")
	val causerType: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("avg_rating")
	val avgRating: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("email")
	val email: Any? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("lng")
	val lng: String? = null,

	@field:SerializedName("building_no")
	val buildingNo: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("phone_number2")
	val phoneNumber2: Any? = null,

	@field:SerializedName("min_price_product")
	val minPriceProduct: Any? = null,

	@field:SerializedName("registration_file2")
	val registrationFile2: Any? = null,

	@field:SerializedName("causer_id")
	val causerId: Any? = null,

	@field:SerializedName("offer_flag")
	val offerFlag: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("apartment_no")
	val apartmentNo: Any? = null,

	@field:SerializedName("registration_image2")
	val registrationImage2: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fixed_commission_price")
	val fixedCommissionPrice: Any? = null,

	@field:SerializedName("registration_image")
	val registrationImage: Any? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Int? = null,

	@field:SerializedName("new_flag")
	val newFlag: Int? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: Any? = null,

	@field:SerializedName("is_featured")
	val isFeatured: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("delivery_price")
	val deliveryPrice: Int? = null
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

data class Shipping(

	@field:SerializedName("payment_status")
	val paymentStatus: String? = null,

	@field:SerializedName("shipping_address")
	val shippingAddress: ShippingAddress? = null,

	@field:SerializedName("payment_reference")
	val paymentReference: String? = null,

	@field:SerializedName("gateway")
	val gateway: String? = null
)
