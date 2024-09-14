package com.brandsin.store.ui.main.offersAndFeatures.model

import com.google.gson.annotations.SerializedName

data class OfferAndFeatureDetailsResponse(
	@field:SerializedName("data")
	val offerAndFeatureItem: OfferAndFeatureItem? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)
