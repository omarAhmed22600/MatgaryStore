package com.brandsin.store.model.main.reports

import com.google.gson.annotations.SerializedName

data class ReportsDetailsResponse(

	@field:SerializedName("data")
	val data: List<DetailsItem?>? = null
)

data class ValueItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("driver_id")
	val driverId: Int? = null,

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("completed_total")
	val completedTotal: String? = null,

	@field:SerializedName("comments")
	val comments: List<Any?>? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("is_rated")
	val isRated: Boolean? = null,

	@field:SerializedName("count")
	val count: String? = null,

	@field:SerializedName("completed_count")
	val completedCount: String? = null,
)

data class DetailsItem(

	@field:SerializedName("date")
    var date: String? = null,

	@field:SerializedName("value")
	val value: List<ValueItem?>? = null
)
