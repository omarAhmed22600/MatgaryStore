package com.brandsin.store.ui.main.marketingRequest

data class MarketingRequest(
    val context: String,
    val type: String,
    val story: List<String>, // ["1087", "1079"]
    val start_date: String,  // "2024-08-30 11:00:00"
    val end_date: String,    // "2024-09-05 11:00:00"
    val number_of_shopping_days: Int,
    val price: Double
)
