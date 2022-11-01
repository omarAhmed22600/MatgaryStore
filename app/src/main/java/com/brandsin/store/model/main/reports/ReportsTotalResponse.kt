package com.brandsin.store.model.main.reports

import com.google.gson.annotations.SerializedName

data class ReportsTotalResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)

data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("comments")
	val comments: List<Any?>? = null,

	@field:SerializedName("is_rated")
	val isRated: Boolean? = null,

	@field:SerializedName("count")
	val count: String? = null
)
