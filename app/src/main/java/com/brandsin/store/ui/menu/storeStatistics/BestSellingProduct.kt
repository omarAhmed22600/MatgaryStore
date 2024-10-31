package com.brandsin.store.ui.menu.storeStatistics

import com.google.gson.annotations.SerializedName

data class BestSellingProduct(
    val price: Int = -1,
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("total_orders")
    val totalOrders: Int = -1,
    val image:String = ""
)