package com.brandsin.store.ui.menu.storeStatistics

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("city_name")
    val cityName: String = "",
    @SerializedName("order_count")
    val orderCount: Int = -1
)