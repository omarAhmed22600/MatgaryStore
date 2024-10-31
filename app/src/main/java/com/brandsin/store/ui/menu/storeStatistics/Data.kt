package com.brandsin.store.ui.menu.storeStatistics

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("best_selling_products")
    val bestSellingProducts: List<BestSellingProduct> = listOf(),
    val cities: List<City> = listOf(),
    val statistics: Statistics = Statistics(),
    @SerializedName("total_products")
    val totalProducts: Int = -1
)