package com.brandsin.store.ui.main.subscriptions

import com.google.gson.annotations.SerializedName

data class SubscriptionListResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("plans")
	val subscriptionPlans: List<SubscriptionPlansItem?>? = null
)

data class SubscriptionPlansItem( // SubscriptionItem

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("subscription_exists")
    var isSelected: Boolean? = null,

	/*
	 1- isSelected == ture | false
	 2- isSelected == false

	 3- isSelected == true
	 */

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("days")
	val days: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("end_date")
	val endDate: String? = null,
)
