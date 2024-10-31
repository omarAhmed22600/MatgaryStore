package com.brandsin.store.ui.menu.storeStatistics

data class Product(
    val productId: String,
    val title: String,
    private val price: String,
    val imageRes: String, // Resource ID for the image,
    val totalOrdered:String
) {
    val formattedPrice = "$price SR"
}