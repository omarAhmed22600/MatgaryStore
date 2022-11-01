package com.brandsin.store.model.menu.commonquest

import com.google.gson.annotations.SerializedName

data class CommonQuesResponse(

		@field:SerializedName("data")
	val questionsList: List<CommonQuestionItem?>? = null,

		@field:SerializedName("success")
	val isSuccess: Boolean? = null
)

data class CommonQuestionItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	var isSelected: Boolean = false
)
