package com.brandsin.store.ui.menu.wallet.model

import com.google.gson.annotations.SerializedName

data class WalletTransactionsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class Store(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Orders(

	@field:SerializedName("ordersInCashCount")
	val ordersInCashCount: Int? = null,

	@field:SerializedName("comments")
	val comments: List<Any?>? = null,

	@field:SerializedName("ordersInVisaTotal")
	val ordersInVisaTotal: String? = null,

	@field:SerializedName("ordersInCashTotal")
	val ordersInCashTotal: String? = null,

	@field:SerializedName("is_rated")
	val isRated: Boolean? = null,

	@field:SerializedName("ordersInVisaCount")
	val ordersInVisaCount: Int? = null
)

data class Data(

	@field:SerializedName("balance")
	val balance: Int? = null,

	@field:SerializedName("orders")
	val orders: Orders? = null,

	@field:SerializedName("store")
	val store: Store? = null,

	@field:SerializedName("transactions")
	val transactions: ArrayList<TransactionsItem?>? = null,

	@field:SerializedName("balance_formated")
	val balanceFormated: String? = null,

	@field:SerializedName("app_amount")
	val appAmount: String? = null,

	@field:SerializedName("store_amount")
	val storeAmount: String? = null,

	@field:SerializedName("order_amount")
	val orderAmount: String? = null,

	@field:SerializedName("app_commission")
	val appCommission: Int? = null,

	@field:SerializedName("payment_method")
	val paymentMethod: String? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class TransactionsItem(

	@field:SerializedName("store_id")
	val storeId: Int? = null,

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("current_credit")
	val currentCredit: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("increase")
	val increase: String? = null,

	@field:SerializedName("order_id")
	val orderId: Int? = null,

	@field:SerializedName("created_by")
	val createdBy: Int? = null,

	@field:SerializedName("decrease")
	val decrease: String? = null
)
