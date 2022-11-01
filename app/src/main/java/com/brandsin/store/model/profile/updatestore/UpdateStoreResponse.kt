package com.brandsin.store.model.profile.updatestore

import com.google.gson.annotations.SerializedName
import com.brandsin.store.model.auth.register.StoreModel

data class UpdateStoreResponse(

	@field:SerializedName("success")
	val isSuccess: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("store")
	val store: StoreModel? = null
)
