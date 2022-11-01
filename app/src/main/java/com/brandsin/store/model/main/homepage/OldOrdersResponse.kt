package com.brandsin.store.model.main.homepage

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.StoreModel
import java.io.Serializable

data class OldOrdersResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val ordersList: List<StoreOrderItem?>? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null,

	@field:SerializedName("limit")
	val limit: String? = null
) : Serializable

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("picture_thumb")
	val pictureThumb: String? = null
) : Serializable

data class Store(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("media")
	val media: List<Any?>? = null
) : Serializable

data class StoreOrderItem(

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("order_number")
	val orderNumber: String? = null,

	@field:SerializedName("user_notes")
	val userNotes: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("delivery_time")
	val deliveryTime: String? = null,

	@field:SerializedName("store")
	val store: StoreModel? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Serializable
