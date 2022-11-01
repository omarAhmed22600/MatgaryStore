package com.brandsin.store.model.main.offers.add

import com.google.gson.annotations.SerializedName

data class OfferAddProductRequest (

    @SerializedName("store_id")
    var store_id: Int? = null,

    @SerializedName("autocomplete")
    var autocomplete: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("lang")
    var lang: String? = null
)