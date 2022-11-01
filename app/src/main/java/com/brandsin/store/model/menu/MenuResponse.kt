package com.brandsin.store.model.menu

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.StoreModel
import com.brandsin.store.model.auth.register.UserModel

data class MenuResponse(
	@field:SerializedName("success")
	var isSuccess: Boolean? = null,

	@field:SerializedName("store")
	var store: StoreModel? = null,

	@field:SerializedName("user")
	var user: UserModel? = null,

	@field:SerializedName("message")
	var message: String? = null
)
