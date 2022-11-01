package com.brandsin.store.model.profile.updateprofile

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.UserModel

data class UpdateProfileResponse(
		@field:SerializedName("success")
		val success: Boolean? = null,
		@field:SerializedName("error")
		val error: String? = null,
		@field:SerializedName("data")
		val data: UserModel? = null
)
