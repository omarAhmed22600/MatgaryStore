package com.brandsin.store.ui.main.refundableProduct.model

import com.google.gson.annotations.SerializedName

data class RefundableDetailsResponse(

    @field:SerializedName("data")
    val refundableDetails: RefundableDetails? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
)

data class Shipping(

    @field:SerializedName("payment_status")
    val paymentStatus: String? = null,

    @field:SerializedName("tracking_number")
    val trackingNumber: Any? = null,

    @field:SerializedName("shipping_address")
    val shippingAddress: ShippingAddress? = null,

    @field:SerializedName("payment_reference")
    val paymentReference: String? = null,

    @field:SerializedName("deliveryOptionId")
    val deliveryOptionId: Any? = null,

    @field:SerializedName("gateway")
    val gateway: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("label_url")
    val labelUrl: Any? = null
)

data class City(

    @field:SerializedName("code")
    val code: Any? = null,

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

    @field:SerializedName("created_by")
    val createdBy: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null
)

data class User(

    @field:SerializedName("national_id")
    val nationalId: List<Any?>? = null,

    @field:SerializedName("integration_id")
    val integrationId: Any? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("birthdate")
    val birthdate: String? = null,

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("confirmation_code")
    val confirmationCode: Any? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("android_token")
    val androidToken: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("ios_token")
    val iosToken: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("card_last_four")
    val cardLastFour: Any? = null,

    @field:SerializedName("card_brand")
    val cardBrand: Any? = null,

    @field:SerializedName("notification_preferences")
    val notificationPreferences: Any? = null,

    @field:SerializedName("provider_fb")
    val providerFb: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("confirmed_at")
    val confirmedAt: Any? = null,

    @field:SerializedName("job_title")
    val jobTitle: Any? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("address")
    val address: Any? = null,

    @field:SerializedName("phone_country_code")
    val phoneCountryCode: Any? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("created_by")
    val createdBy: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("reward_points")
    val rewardPoints: Any? = null,

    @field:SerializedName("picture")
    val picture: String? = null,

    @field:SerializedName("picture_thumb")
    val pictureThumb: String? = null,

    @field:SerializedName("wallet_credit")
    val walletCredit: Double? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("updated_by")
    val updatedBy: Int? = null,

    @field:SerializedName("provider_id")
    val providerId: Any? = null,

    @field:SerializedName("online")
    val online: Int? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("trial_ends_at")
    val trialEndsAt: Any? = null,

    @field:SerializedName("payment_method_token")
    val paymentMethodToken: Any? = null,

    @field:SerializedName("gateway")
    val gateway: Any? = null,

    @field:SerializedName("properties")
    val properties: Properties? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class ItemOptions(

    @field:SerializedName("product_options")
    val productOptions: String? = null
)

data class Properties(

    @field:SerializedName("about")
    val about: Any? = null
)

data class ShippingAddress(

    @field:SerializedName("zip")
    val zip: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    // @field:SerializedName("city")
    // val city: City? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("address_1")
    val address1: String? = null,

    @field:SerializedName("address_2")
    val address2: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    // @field:SerializedName("state")
    // val state: State? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("landmark")
    val landmark: String? = null,

    @field:SerializedName("street_name")
    val streetName: String? = null
)

data class State(

    @field:SerializedName("order_number")
    val id: Int? = null,

    @field:SerializedName("order_number")
    val name: String? = null,

    @field:SerializedName("order_number")
    val orderNumber: String? = null,

    @field:SerializedName("city_id")
    val cityId: Int? = null,

    @field:SerializedName("country_id")
    val countryId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,
)

data class Order(

    @field:SerializedName("driver_id")
    val driverId: Any? = null,

    @field:SerializedName("order_number")
    val orderNumber: String? = null,

    @field:SerializedName("user_notes")
    val userNotes: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("device_type")
    val deviceType: String? = null,

    @field:SerializedName("delivery_time")
    val deliveryTime: String? = null,

    @field:SerializedName("billing")
    val billing: Billing? = null,

    @field:SerializedName("statuses_log")
    val statusesLog: List<StatusesLogItem?>? = null,

    @field:SerializedName("preparing_time_store")
    val preparingTimeStore: Any? = null,

    @field:SerializedName("shipping")
    val shipping: Shipping? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("packaging_price")
    val packagingPrice: String? = null,

    @field:SerializedName("gifter_name")
    val gifterName: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("order_type")
    val orderType: String? = null,

    @field:SerializedName("discount_id")
    val discountId: Int? = null,

    @field:SerializedName("store_id")
    val storeId: Int? = null,

    @field:SerializedName("amount")
    val amount: String? = null,

    @field:SerializedName("orders_key")
    val ordersKey: String? = null,

    @field:SerializedName("comments")
    val comments: List<Any?>? = null,

    @field:SerializedName("address_id")
    val addressId: Int? = null,

    @field:SerializedName("discount_value")
    val discountValue: String? = null,

    @field:SerializedName("created_by")
    val createdBy: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("has_packaging")
    val hasPackaging: Int? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("is_rated")
    val isRated: Boolean? = null,

    @field:SerializedName("updated_by")
    val updatedBy: Int? = null,

    @field:SerializedName("delivery_time_store")
    val deliveryTimeStore: Any? = null,

    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("transaction_no")
    val transactionNo: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("gifter_mobile")
    val gifterMobile: String? = null
)

data class RefundableDetails(

    @field:SerializedName("note")
    val note: String? = null,

    @field:SerializedName("reason")
    val reason: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("approval")
    val approval: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("video")
    val video: Any? = null,

    @field:SerializedName("created_by")
    val createdBy: Int? = null,

    @field:SerializedName("reason_id")
    val reasonId: Int? = null,

    @field:SerializedName("order_item_id")
    val orderItemId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("order_item")
    val orderItem: OrderItem? = null,

    @field:SerializedName("product_id")
    val productId: Int? = null,

    @field:SerializedName("updated_by")
    val updatedBy: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class StatusesLogItem(

    @field:SerializedName("time")
    val time: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class OrderItem(

    @field:SerializedName("amount")
    val amount: String? = null,

    @field:SerializedName("item_options")
    val itemOptions: ItemOptions? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("user_notes")
    val userNotes: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("created_by")
    val createdBy: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("updated_by")
    val updatedBy: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("sku_code")
    val skuCode: String? = null,

    @field:SerializedName("order_id")
    val orderId: Int? = null,

    @field:SerializedName("order")
    val order: Order? = null
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

    @field:SerializedName("landmark")
    val landmark: String? = null,

    @field:SerializedName("street_name")
    val streetName: String? = null
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
