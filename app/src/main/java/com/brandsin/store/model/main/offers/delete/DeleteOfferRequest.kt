package com.brandsin.store.model.main.offers.delete

import com.google.gson.annotations.SerializedName

data class DeleteOfferRequest(
    @SerializedName("offer_id")
    var offerId: Int? = null
)