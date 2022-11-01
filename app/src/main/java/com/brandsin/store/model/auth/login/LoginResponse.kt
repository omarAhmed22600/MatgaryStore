package com.brandsin.store.model.auth.login

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.StoreModel
import com.brandsin.store.model.auth.register.UserModel

data class LoginResponse(

	@field:SerializedName("success")
	var isSuccess: Boolean? = null,

	@field:SerializedName("store")
	var store: StoreModel? = null,

	@field:SerializedName("user")
	var user: UserModel? = null,

	@field:SerializedName("message")
	var message: String? = null
)