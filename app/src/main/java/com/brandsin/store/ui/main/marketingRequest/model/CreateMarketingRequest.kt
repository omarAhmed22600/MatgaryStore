package com.brandsin.store.ui.main.marketingRequest.model

import com.google.gson.annotations.SerializedName

data class CreateMarketingRequest(

    @SerializedName("type")
    val type: String,

    @SerializedName("story")
    val story: ArrayList<String>,

    @SerializedName("number_of_shopping_days")
    val numberOfShoppingDays: Int,

    @SerializedName("price")
    val price: Double
)