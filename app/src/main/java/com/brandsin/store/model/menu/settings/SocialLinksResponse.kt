package com.brandsin.user.model.menu.settings

import com.google.gson.annotations.SerializedName

data class SocialLinksResponse(

	@field:SerializedName("data")
	val socialLinks: SocialLinks? = null,

	@field:SerializedName("success")
	val isSuccess: Boolean? = null
)

data class SocialLinks(

	@field:SerializedName("twitter")
	val twitter: String? = null,

	@field:SerializedName("facebook")
	val facebook: String? = null,

	@field:SerializedName("pinterest")
	val pinterest: String? = null,

	@field:SerializedName("linkedin")
	val linkedin: String? = null,

	@field:SerializedName("instagram")
	val instagram: String? = null
)
