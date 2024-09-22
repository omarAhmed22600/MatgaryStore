package com.brandsin.store.ui.menu.storeStatistics

data class Product(
    val productId: String,
    val title: String,
    val price: String,
    val imageResId: Int // Resource ID for the image
)